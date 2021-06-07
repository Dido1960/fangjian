package com.ejiaoyi.agency.service.impl;

import com.ejiaoyi.agency.dto.SiteBidDecryptDto;
import com.ejiaoyi.agency.service.ISiteOpenBidService;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.*;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 现场标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-07-07
 */
@Slf4j
@Service
public class SiteOpenBidServiceImpl extends BaseServiceImpl implements ISiteOpenBidService {

    @Value("${dock.gtxd.lz.url}")
    private String lzGtXdDockUrl;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IDownloadService downloadService;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private BidderOpenInfoServiceImpl bidderOpenInfoService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private BidderFileUploadServiceImpl bidderFileUploadService;

    @Autowired
    private IClearBidV3Service clearBidService;

    @Override
    public void parseBidderProject(Integer bidSectionId, Integer bidderFileId) throws Exception {
        String customFilePath = FileUtil.getDecryptBidderFilePath(bidderFileId.toString());
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getUploadById(bidderFileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            Fdfs fdfs = fdfsService.downloadByUpload(bidderFileId);
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + gef.getSuffix();
            // 文件下载
            FileUtil.writeFile(fdfs.getBytes(), outPath);
            //downloadService.multiThreadDownload(bidFilePath, outPath, 3);
        } else {
            outPath = localPath;
        }
        // 解压
        boolean unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解压失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            throw new CustomException("投标文件解密失败！");
        } else {
            // 文件解压成功之后，验证pdf是否破损
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    throw new CustomException("投标PDF文件破损！");
                }
            }
            //判断招标解密是否解密成功
            parseBidFileInfo(unzipPath, bidSectionId);

            // 解析投标项目相关信息
            String bidRelateInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_RELATE_CONTENT;
            Map<String, String> tenderRelateInfo = TenderXmlUtil.parseTenderRelateInfo(bidRelateInfoPath);
            if (CommonUtil.isEmpty(tenderRelateInfo)) {
                throw new CustomException("投标文件解密失败！");
            }
            // 投标文件项目类型
            String tenderClassifyCode = tenderRelateInfo.get("bidClassifyCode");
            BidProtype bidProtype = BidProtype.getBidProtypeName(tenderClassifyCode);

            // 解析投标标项目信息
            String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_INFO;
            Map<String, String> bidProjectInfo = TenderXmlUtil.parseTenderDetailInfo(bidProjectInfoPath, bidProtype);

            // xml投标工期
            String totalTimeLimit = bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_TIME_LIMIT);
            // xml投标工程质量信息
            String qualityInformation = bidProjectInfo.get(BidInfoTemplateConstant.QUALITY_INFORMATION);
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            String bidPrice;
            // 是否是施工类标段
            boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());

            if (isConstruction) {
                // 解析清单报价更新到数据库
                String bidProjectInfoXml = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoXml);
            } else {
                // xml投标报价
                bidPrice = bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_QUOTE);
            }

            // xml 投标人名称
            String bidderName = bidProjectInfo.get(BidInfoTemplateConstant.BIDDER);
            String bidderCoCode = bidProjectInfo.get(BidInfoTemplateConstant.BIDDER_CO_CODE);

            // 校验投标是否存在
            List<Bidder> oldList = bidderService.getBidder(bidderCoCode, bidSectionId);
            if (!CollectionUtils.isNotEmpty(oldList) && oldList.size() > 0) {
                throw new CustomException("投标人已经存在==>【投标人名称：“" + bidderName + "”，投标人组织机构代码：“" + bidderCoCode + "”】");
            }
            // 1、添加投标人
            List<Bidder> bidders = bidderService.getBidder(bidderCoCode, bidSectionId);
            if (CollectionUtils.isNotEmpty(bidders) && bidders.size() > 0) {
                throw new CustomException("经检测，您导入的投标文件解析的投标人已存在【存在的投标人名称：“" + bidders.get(0).getBidderName() + "”，投标人统一社会信用代码：“" + bidders.get(0).getBidderOrgCode() + "”】");
            }
            Integer bidderId = bidderService.saveBidder(Bidder.builder()
                    .bidSectionId(bidSectionId)
                    .bidderName(bidderName)
                    .bidDocId(bidderFileId)
                    .bidDocType(FileType.GEFORTJY.getType())
                    .bidderOrgCode(bidderCoCode)
                    .build());

            // 2、添加投标人开标信息
            BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                    .bidSectionId(bidSection.getId())
                    .bidderId(bidderId)
                    .decryptStartTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .decryptEndTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .clientName(bidProjectInfo.get(BidInfoTemplateConstant.LEGAL_AGENT))
                    .tenderDecryptStatus(1)
                    .decryptStatus(1)
                    .marginPayStatus(1)
                    .bidPrice(bidPrice)
                    .bidPriceType(bidProjectInfo.get(BidInfoTemplateConstant.QUOTE_TYPE))
                    .rate(bidProjectInfo.get(BidInfoTemplateConstant.RATE))
                    .timeLimit(totalTimeLimit)
                    .build();
            // 施工添加,工程质量
            bidderOpenInfo.setQuality(qualityInformation);
            bidderOpenInfoService.insert(bidderOpenInfo);
            bidderFileInfoService.initBidderFileInfo(BidderFileInfo.builder()
                    .bidderId(bidderId)
                    .gefId(bidderFileId)
                    .fileUnzipPath(unzipPath)
                    .build());

            ThreadUtlis.run(() -> {
                bidderFileUploadService.bidderFileUpload(bidderId, bidderFileId, new File(unzipPath), isConstruction);
            });
            try {
                // 解析评审点
                String reviewPoint = unzipPath + File.separator + TenderFileConstant.JUDGING_POINT_XML;
                bidderFileInfoService.saveBidderReviewPoint(reviewPoint, bidderId, bidSectionId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SiteBidDecryptDto siteDecryptGef(Integer fileId, Integer bidderId, Integer bidSectionId) throws Exception {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getUploadById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.GEFORTJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes,outPath);
//            downloadService.multiThreadDownload(fdfsFilePath, outPath);
        } else {
            outPath = localPath;
        }
        // 解压
        boolean unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfoService.updateById(BidderOpenInfo.builder()
                    .id(bidderOpenInfo.getId())
                    .decryptStatus(2)
                    .tenderDecryptStatus(2)
                    .build());
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(true)
                    .decryptMsg("投标文件解密失败！")
                    .build();
        } else {
            return updateBidderAllInfo(fileId, bidSectionId,bidderId, 0);
        }
    }

    @Override
    public SiteBidDecryptDto siteDecryptSgef(Integer fileId, Integer bidderId, Integer bidSectionId, String privateKey, String isOtherCa) throws Exception {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        if (Enabled.NO.getCode().toString().equals(isOtherCa)) {
            UploadFile gef = uploadFileService.getById(fileId);
            String localPath = FileUtil.getCustomFilePath() + gef.getPath();
            // 解压文件路径
            String outPath;
            if (!new File(localPath).exists()) {
                //fdfs路径
                String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
                //下载路径
                outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
                // 文件下载
                byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
                FileUtil.writeFile(bytes,outPath);
//                downloadService.multiThreadDownload(fdfsFilePath, outPath);
            } else {
                outPath = localPath;
            }

            // sgef信封解密
            CertTools.fileDecoder(new File(outPath), cipherPath, privateKey);
        }

        //解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfoService.updateById(BidderOpenInfo.builder()
                    .id(bidderOpenInfo.getId())
                    .decryptStatus(2)
                    .tenderDecryptStatus(2)
                    .build());
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(true)
                    .decryptMsg("投标文件解密失败！")
                    .build();
        } else {
            return updateBidderAllInfo(fileId, bidSectionId,bidderId, 1);
        }
    }

    @Override
    public DecoderCipherInfoDTO getDecoderCipherInfo(Integer fileId) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getUploadById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes,outPath);
//            downloadService.multiThreadDownload(fdfsFilePath, outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        return CertTools.otherFileDecoder(new File(outPath), cipherPath);
    }

    /**
     * 解析XML投标文件
     *
     * @param unzipPath 解压路径
     * @param bidSectionId    投标人
     * @throws Exception
     */
    private void parseBidFileInfo(String unzipPath, Integer bidSectionId) throws Exception {
        // 解析投标项目相关信息
        String bidRelateInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_RELATE_CONTENT;
        Map<String, String> tenderRelateInfo = TenderXmlUtil.parseTenderRelateInfo(bidRelateInfoPath);
        if (CommonUtil.isEmpty(tenderRelateInfo)) {
            throw new CustomException("投标文件解密失败！");
        }
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        // 投标文件项目类型
        String tenderClassifyCode = tenderRelateInfo.get("bidClassifyCode");
        BidProtype bidProtype = BidProtype.getBidProtypeName(tenderClassifyCode);

        // 解析投标标项目信息
        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_INFO;
        Map<String, String> bidProjectInfo = TenderXmlUtil.parseTenderDetailInfo(bidProjectInfoPath, bidProtype);

        //判断投标文件的 招标解密状态
        String bidCode = bidProjectInfo.get(BidInfoTemplateConstant.SECT_CODE);
        boolean bidCodeFlag = bidCode.equals(bidSection.getBidSectionCode());
        boolean claCodeFlag = tenderClassifyCode.equals(bidSection.getBidClassifyCode());
        System.out.println("对比文件是否该项目：" + (bidCodeFlag && claCodeFlag));
        BidProtype bidProtypeEnum = BidProtype.getBidProtypeByCode(tenderClassifyCode);
        String proType = "";
        if (bidProtypeEnum != null) {
            proType = bidProtypeEnum.getChineseName();
        }
        if (!(bidCodeFlag && claCodeFlag)) {
            throw new CustomException("非该标段投标文件【文件解析内容为：标段编号“" + bidCode + "”，标段类型“" + proType + "”】");
        }
    }

    /**
     * 保存投标人信息
     * @param fileId 文件id
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @param fileType 文件类型
     * @return
     * @throws Exception
     */
    private SiteBidDecryptDto updateBidderAllInfo(Integer fileId, Integer bidSectionId, Integer bidderId, Integer fileType) throws Exception {
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        // 文件解压成功之后，验证pdf是否破损
        List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
        for (File file : files) {
            if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                return SiteBidDecryptDto.builder()
                        .decryptStatus(false)
                        .nameConsistentStatus(true)
                        .decryptMsg("投标PDF文件破损！")
                        .build();
            }
        }
        //判断招标解密是否解密成功
        parseBidFileInfo(unzipPath, bidSectionId);

        // 解析投标项目相关信息
        String bidRelateInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_RELATE_CONTENT;
        Map<String, String> tenderRelateInfo = TenderXmlUtil.parseTenderRelateInfo(bidRelateInfoPath);
        if (CommonUtil.isEmpty(tenderRelateInfo)) {
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(true)
                    .decryptMsg("投标文件解密失败！")
                    .build();
        }
        // 投标文件项目类型
        String tenderClassifyCode = tenderRelateInfo.get("bidClassifyCode");
        BidProtype bidProtype = BidProtype.getBidProtypeName(tenderClassifyCode);

        // 解析投标标项目信息
        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_INFO;
        Map<String, String> bidProjectInfo = TenderXmlUtil.parseTenderDetailInfo(bidProjectInfoPath, bidProtype);

        // xml投标工期
        String totalTimeLimit = bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_TIME_LIMIT);
        // xml投标工程质量信息
        String qualityInformation = bidProjectInfo.get(BidInfoTemplateConstant.QUALITY_INFORMATION);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        String bidPrice;
        // 是否是施工类标段
        boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());

        if (isConstruction) {
            // 解析清单报价更新到数据库
            String bidProjectInfoXml = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
            bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoXml);
        } else {
            // xml投标报价
            bidPrice = bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_QUOTE);
        }

        // xml 投标人名称
        String bidderName = bidProjectInfo.get(BidInfoTemplateConstant.BIDDER);
        String bidderCoCode = bidProjectInfo.get(BidInfoTemplateConstant.BIDDER_CO_CODE);

        Bidder updateBidder = Bidder.builder()
                .id(bidderId)
                .bidderName(bidderName)
                .bidDocId(fileId)
                .bidDocType(fileType)
                .bidderOrgCode(bidderCoCode)
                .build();

        BidderOpenInfo updateObj = BidderOpenInfo.builder()
                .id(bidderOpenInfo.getId())
                .decryptStartTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .decryptEndTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .clientName(bidProjectInfo.get(BidInfoTemplateConstant.LEGAL_AGENT))
                .tenderDecryptStatus(1)
                .decryptStatus(1)
                .marginPayStatus(1)
                .bidPrice(bidPrice)
                .bidPriceType(bidProjectInfo.get(BidInfoTemplateConstant.QUOTE_TYPE))
                .rate(bidProjectInfo.get(BidInfoTemplateConstant.RATE))
                .timeLimit(totalTimeLimit)
                .build();
        // 施工添加,工程质量
        updateObj.setQuality(qualityInformation);
        BidderFileInfo build = BidderFileInfo.builder()
                .id(bidderFileInfo.getId())
                .fileUnzipPath(unzipPath)
                .build();
        String decryptMsg;
        if (FileType.SGEFORETJY.getType().equals(fileType)) {
            decryptMsg = "解密成功！";
            build.setSgefId(fileId);
        } else {
            decryptMsg = "校验成功！";
            build.setGefId(fileId);
        }

        // 如果投标文件解析的投标人名称与当前投标人名称不一致，提示用户确认
        if (!bidder.getBidderName().trim().equals(bidderName.trim())) {
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(false)
                    .fileBidderName(bidderName.trim())
                    .dataBaseBidderName(bidder.getBidderName().trim())
                    .updateBidder(updateBidder)
                    .updateBidderOpenInfo(updateObj)
                    .updateBidderFileInfo(build)
                    .isConstruction(isConstruction)
                    .decryptMsg("投标文件解析的投标人名称与当前投标人名称不一致！")
                    .build();
        }

        // 1、更新投标人
        bidderService.updateBidderById(updateBidder);
        // 2、更新投标人开标信息
        bidderOpenInfoService.updateBidderOpenInfoById(updateObj);
        // 3、更新投标人文件信息
        bidderFileInfoService.updateById(build);

        ThreadUtlis.run(() -> {
            bidderFileUploadService.bidderFileUpload(bidderId, fileId, new File(unzipPath), isConstruction);
        });

        try {
            // 解析评审点
            String reviewPoint = unzipPath + File.separator + TenderFileConstant.JUDGING_POINT_XML;
            bidderFileInfoService.saveBidderReviewPoint(reviewPoint, bidderId, bidSectionId, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SiteBidDecryptDto.builder()
                .decryptStatus(true)
                .nameConsistentStatus(true)
                .decryptMsg(decryptMsg)
                .build();
    }

    @Override
    public List<String> listBidOpenProcessComplete(Integer id) {
        List<String> process = new ArrayList<>();
        BidSection section = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);

        if (BidProtype.EPC.getCode().equals(section.getBidClassifyCode())) {
            // 最高投标限价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(StaffBidOpenFlow.CONTROL_PRICE.getName());
                process.add(StaffBidOpenFlow.BIDDER_FILE_DECRYPT.getName());
                // 控制价分析
                process.add(StaffBidOpenFlow.CONTROL_PRICE_ANALYSIS.getName());
                if (process.size() > 2) {
                    process.add(StaffBidOpenFlow.FILE_CURSOR.getName());
                }
            }

        } else if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())) {
            // 招标控制价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(StaffBidOpenFlow.CONTROL_PRICE.getName());
            }

            // 浮动点
            if (StringUtil.isNotEmpty(tenderDoc.getFloatPoint())) {
                process.add(StaffBidOpenFlow.FLOAT_POINT.getName());
                process.add(StaffBidOpenFlow.BIDDER_FILE_DECRYPT.getName());
                // 控制价分析
                process.add(StaffBidOpenFlow.CONTROL_PRICE_ANALYSIS.getName());
            }

            if (process.size() > 3) {
                process.add(StaffBidOpenFlow.FILE_CURSOR.getName());
            }
        } else {
            process.add(StaffBidOpenFlow.BIDDER_FILE_DECRYPT.getName());
            if (process.size() > 0) {
                process.add(StaffBidOpenFlow.FILE_CURSOR.getName());
            }
        }

        // 开标记录表
        String bidOpenRecordFileMark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + section.getId() + File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + ".pdf";
        String urlByMark = fdfsService.getUrlByMark(bidOpenRecordFileMark);
        if (StringUtil.isNotEmpty(urlByMark)) {
            process.add(StaffBidOpenFlow.BID_OPEN_RECORD.getName());
        }

        BidderFileUploadDTO bidderFileUploadDTO = bidderFileInfoService.listBidderFileUpload(id);
        if (!CommonUtil.isEmpty(bidderFileUploadDTO) && bidderFileUploadDTO.getBidderNum().equals(bidderFileUploadDTO.getUploadSuccessNum())) {
//            process.add(StaffBidOpenFlow.BID_FILE_UPLOAD.getName());
        }

        // 开标结束
        if (Status.END.getCode().equals(section.getBidOpenStatus())) {
            process.add(StaffBidOpenFlow.BID_OPEN_END.getName());
            // 复会时间
            if (StringUtil.isNotEmpty(section.getResumeTime())) {
                process.add(StaffBidOpenFlow.RESUME_TIME.getName());
            }

        }
        return process;
    }

}
