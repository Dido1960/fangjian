package com.ejiaoyi.bidder.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.bidder.service.IBidFileService;
import com.ejiaoyi.common.constant.CzrConstant;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.CurrentScheduleDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.RegServiceImpl;
import com.ejiaoyi.common.service.impl.SignatureConfigInfoServiceImpl;
import com.ejiaoyi.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-7-30 09:52
 */
@Service
public class BidFileServiceImpl extends BaseServiceImpl implements IBidFileService {

    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private IBsnChainInfoService bsnChainInfoService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private RegServiceImpl regService;
    @Autowired
    private SignatureConfigInfoServiceImpl signatureConfigInfoService;

    @Override
    public JsonData fileJudge(BidderOpenInfo bidderOpenInfo) {

        //设置Redis 当前已开始文件递交
        String redisKey = "BEFORE_SAVE_FILE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        RedisUtil.set(redisKey, true);

        //获取文件递交流程DTO
        List<CurrentScheduleDTO> currentScheduleS = FileSchedule.listCurrentSchedule();
        String scheduleKey = "CURRENT_SCHEDULE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        RedisUtil.set(scheduleKey, currentScheduleS);

        JsonData result = new JsonData();

        String gefMD5 = null;
        String sgefMD5 = null;
        String gefHash = null;
        String sgefHash = null;
        String czrMD5 = null;

        String certId = null;
        String caType = null;
        String cipher = null;
        String sectCode = null;
        String projType = null;

        //不一定上传的文件返回比对状态
        Boolean gefResult = true;
        //必须上传的文件返回比对状态
        Boolean sgefResult = false;

        try {

            //判断是否在文件上传时间内
            Map<String, String> times = this.getBidSectionFileUploadTimes(bidderOpenInfo.getBidSectionId());

            if ("false".equals(times.get("canDoIt"))){
                result.setCode("2");
                result.setMsg("文件上传失败，不在文件上传时间内！");
                return result;
            }

            //解析czr获取对应hash值
            if (bidderOpenInfo.getBidderFileInfo().getCzrId() != null) {
                Fdfs fdfs = fdfsService.downloadByUpload(bidderOpenInfo.getBidderFileInfo().getCzrId());
                czrMD5 = FileUtil.getFileMD5(fdfs.getBytes());
                gefHash = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.GEF_HASH);
                sgefHash = BidXmlUtil.getAttrByBytes(fdfs.getBytes(),CzrConstant.SGEF_HASH);
                certId = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.CA_NUM);
                caType = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.CA_TYPE);
                cipher = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.CIPHER).replaceAll("\\n","").trim();
                sectCode = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.SECT_CODE);
                projType = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.PROJ_TYPE);

                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.CZR_PARSING.getIndex(), Status.PROCESSING.getCode());
            }

            //判断文件是否为当前标段文件
            if (!judjeThisBidSection(bidderOpenInfo.getBidSectionId(), sectCode, projType)) {
                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.BID_CONSISTENT.getIndex(), Status.END.getCode());

                result.setCode("2");
                result.setMsg("文件上传失败，经检测您上传的文件信息与该标段信息不符合，请重新上传");
                return result;
            }

            scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.BID_CONSISTENT.getIndex(), Status.PROCESSING.getCode());
            /**
             * 逻辑分析：
             * 两者与运算为返回值
             * gef不一定上传默认为true，如果sgef比对成功返回true
             * gef比对不成功，或者上传了，计算出错等都改为false
             * sgef必须上传，除非比对成功，否则为false
             */
            //获取MD5值

            if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                Fdfs fdfs = fdfsService.getFdfsByUpload(bidderOpenInfo.getBidderFileInfo().getGefId());
                gefMD5 = fdfs.getFileHash();
            }
            if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                Fdfs fdfs = fdfsService.getFdfsByUpload(bidderOpenInfo.getBidderFileInfo().getSgefId());
                sgefMD5 = fdfs.getFileHash();
            }

            //比对gefHash
            if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                if (gefMD5 != null && gefHash != null) {
                    gefResult = gefHash.equalsIgnoreCase(gefMD5);
                } else {
                    gefResult = false;
                }
            }
            //比对sgefHash
            if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                if (sgefMD5 != null && sgefHash != null) {
                    sgefResult = sgefHash.equalsIgnoreCase(sgefMD5);
                }
            }

            if (gefResult && sgefResult) {
                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.FILE_SEALING.getIndex(), Status.PROCESSING.getCode());
            }else {
                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.FILE_SEALING.getIndex(), Status.END.getCode());
            }

            //数据封装
            if (gefResult && sgefResult) {
                if (gefMD5 != null) {
                    bidderOpenInfo.getBidderFileInfo().setGefHash(gefMD5);
                }
                bidderOpenInfo.getBidderFileInfo().setSgefHash(sgefMD5);
                bidderOpenInfo.getBidderFileInfo().setCzrHash(czrMD5);

                if (certId != null) {
                    bidderOpenInfo.getBidderFileInfo().setCertId(certId);
                }
                bidderOpenInfo.getBidderFileInfo().setCipher(cipher);
                bidderOpenInfo.getBidderFileInfo().setCaType(caType);
                bidderOpenInfo.setUpfileTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.DATA_ENCAPSULATION.getIndex(), Status.PROCESSING.getCode());
            }

            if (gefResult && sgefResult) {
                //上传区块链
                String gefUrl = null;
                String sgefUrl = null;
                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                    gefUrl = bsnChainInfoService.addBidDocumentsWitness(czrMD5, gefMD5, ".gef", bidderOpenInfo.getBidderId());
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                    sgefUrl = bsnChainInfoService.addBidDocumentsWitness(czrMD5, sgefMD5, ".sgef", bidderOpenInfo.getBidderId());
                }
                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.DATA_UPLOAD.getIndex(), Status.PROCESSING.getCode());

                String gefQrPathtoPdf = null;
                String sgefQrPathtoPdf = null;
                String gefQrPath = null;
                String sgefQrPath = null;
                String qrLofoPath = FileUtil.getProjectResourcePath() + File.separator + "img" + File.separator + "qrLogo.png";
                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                    UUID gefUuid = UUID.randomUUID();
                    gefQrPathtoPdf = "img/" + bidderOpenInfo.getId() + "-" + gefUuid + ".png";
                    gefQrPath = FileUtil.getProjectResourcePath() + "/" + gefQrPathtoPdf;
                    TwoDimensionCode.encoderQRCode(gefUrl, gefQrPath, "png", 20, qrLofoPath);
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                    UUID sgefUuid = UUID.randomUUID();
                    sgefQrPathtoPdf = "img/" + bidderOpenInfo.getId() + "-" + sgefUuid + ".png";
                    sgefQrPath = FileUtil.getProjectResourcePath() + "/" + sgefQrPathtoPdf;
                    TwoDimensionCode.encoderQRCode(sgefUrl, sgefQrPath, "png", 20, qrLofoPath);
                }

                //模板数据封装
                Map<String, Object> data = getReceiptData(bidderOpenInfo, gefQrPathtoPdf, sgefQrPathtoPdf);
                //生成PDF并上传，存入ID到boi中
                Integer pdfId = createReceiptPDF(data);

                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null && gefUrl != null) {
                    FileUtil.deleteFile(new File(gefQrPath));
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null && gefUrl != null) {
                    FileUtil.deleteFile(new File(sgefQrPath));
                }
                bidderOpenInfo.getBidderFileInfo().setReceiptId(pdfId);

                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.RECEIPT_SYNTHESIS.getIndex(), Status.PROCESSING.getCode());
            }
            if (gefResult && sgefResult) {
                //数据存储
                updateBidderOpenInfo(bidderOpenInfo);
                Bidder bidder = new Bidder();
                bidder.setId(bidderOpenInfo.getBidderId());
                bidder.setBidDocId(bidderOpenInfo.getBidderFileInfo().getSgefId());
                bidder.setBidDocType(FileType.GEFORTJY.getType());
                bidderService.updateBidderById(bidder);
            }

            if (gefResult && sgefResult){
                result.setCode("1");
            } else {
                result.setCode("2");
                result.setMsg("文件上传失败，经检测您上传的投标文件文件与投标备用文件不是同一证书下制作的，请重新上传");
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("2");
            result.setMsg("网络异常,请稍后重试!");
            return result;
        }finally {
            RedisUtil.delete(redisKey);
            RedisUtil.delete(scheduleKey);
        }
    }

    /**
     * 对流程 设置Redis数据
     * @param redisKey redis键
     * @param currentScheduleS 当前流程列表
     * @param index 需要修改的序列号
     * @param status 修改的状态
     */
    private void scheduleRedisSet(String redisKey, List<CurrentScheduleDTO> currentScheduleS, Integer index, Integer status) {
        CurrentScheduleDTO changeDTO = currentScheduleS.get(index);
        changeDTO.setStatus(status);
        currentScheduleS.set(index, changeDTO);
        RedisUtil.set(redisKey, currentScheduleS);
    }

    /**
     * PDF文件数据封装
     *
     * @param bidderOpenInfo uuid :生成二维码路径
     * @return
     */
    private Map<String, Object> getReceiptData(BidderOpenInfo bidderOpenInfo, String gefQrPathtoPdf, String sgefQrPathtoPdf) {
        Map<String, Object> data = new HashMap<>();
        //查询标段数据
        BidSection bidSection = bidSectionService.getBidSectionById(bidderOpenInfo.getBidSectionId());
        BidderFileInfo bidderFileInfo = bidderOpenInfo.getBidderFileInfo();
        data.put("bidSection", bidSection);
        //查询投标人数据
        Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
        data.put("bidder", bidder);
        //查询文件数据
        UploadFile sgefFile = uploadFileService.getById(bidderFileInfo.getSgefId());
        data.put("sgefFile", sgefFile);
        if (bidderFileInfo.getGefId() != null) {
            UploadFile gefFile = uploadFileService.getById(bidderFileInfo.getGefId());
            data.put("gefFile", gefFile);
        }
        //投标人开标信息
        data.put("bidderOpenInfo", bidderOpenInfo);
        data.put("bidderFileInfo", bidderFileInfo);
        //二维码本地地址
        data.put("gefQrPathtoPdf", gefQrPathtoPdf);
        data.put("sgefQrPathtoPdf", sgefQrPathtoPdf);

        return data;
    }

    /**
     * 创建PDF文件
     *
     * @param data
     * @return
     */
    private Integer createReceiptPDF(Map<String, Object> data) {
        //PDF本地文件路径
        String customPath = FileUtil.getCustomFilePath() + "rceiptPDF";
        //以boi ID创建文件夹
        BidderOpenInfo bidderOpenInfo = (BidderOpenInfo) data.get("bidderOpenInfo");
        BidSection bidSection = (BidSection) data.get("bidSection");
        String fdfsUploadPath = customPath + File.separator + bidderOpenInfo.getId();
        UUID uuid = UUID.randomUUID();
        String outPdfPath = fdfsUploadPath + File.separator + ProjectFileTypeConstant.BID_RECEIPT + File.separator + uuid + ".pdf";

        //模板文件路径
        String projectResourcePath = FileUtil.getProjectResourcePath();
        String ftlPath = projectResourcePath + "ftl/bidder/receipt/receipt.ftl";

        //合成PDF
        try {
            PDFUtil.ftlToPdf(ftlPath, outPdfPath, data);
            if (bidSection != null) {
                Reg reg = regService.getRegById(bidSection.getRegId());
                SignatureConfigInfo signatureConfigInfo = signatureConfigInfoService.getSignatureConfigInfoByRegNo(reg.getRegNo());
                if (signatureConfigInfo != null && StringUtils.isNotEmpty(signatureConfigInfo.getImpressionNo())) {
                    SignatureUtil.pdfSignByRuleNum(signatureConfigInfo.getImpressionNo(), outPdfPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //上传PDF
        fdfsService.uploadProjectFile(ProjectFileTypeConstant.BID_RECEIPT, bidderOpenInfo.getId(), new File(outPdfPath));
        //删除本地PDF
        FileUtil.removeDir(new File(customPath));
        //构造FastDfs 文件Mark
        String mark = File.separator + ProjectFileTypeConstant.BID_RECEIPT +
                File.separator + bidderOpenInfo.getId() + File.separator + uuid + ".pdf";
        Fdfs fdfs2 = fdfsService.getFdfsByMark(mark);
        if (fdfs2 == null) {
            return null;
        }
        return fdfs2.getId();
    }

    /**
     * 开标信息数据更新
     *
     * @param bidderOpenInfo
     * @return
     */
    private Integer updateBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        BidderFileInfo bidderFileInfo = bidderOpenInfo.getBidderFileInfo();
        if (CommonUtil.isEmpty(bidderFileInfo.getId())) {
            BidderFileInfo newBf = bidderFileInfoService.getBidderFileInfoByBidderId(bidderOpenInfo.getBidderId());
            bidderFileInfo.setId(newBf.getId());
        }
        bidderFileInfoService.updateById(bidderFileInfo);
        return bidderOpenInfoService.updateById(bidderOpenInfo);
    }

    @Override
    public BidderOpenInfo getBoiByIds(int bidSectionId, int bidderId) {
        return bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
    }

    @Override
    public Map<String, String> getBidSectionTimes(int bidSectionId) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        Map<String, String> map = new HashMap<>();
        String endTime = tenderDoc.getBidDocReferEndTime();
        long timeDiff = DateTimeUtil.getTimeDiff(endTime, tenderDoc.getBidOpenTime(),
                TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (timeDiff >= 0) {
            map.put("endTime", endTime);
        } else {
            map.put("endTime", tenderDoc.getBidOpenTime());
        }
        String signTime = null;
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        Integer signInStartTimeLeft = bidSection.getSignInStartTimeLeft();
        int diff = 0;
        if (signInStartTimeLeft != null) {
            diff = signInStartTimeLeft * 60 * 1000;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode());
            Date parse = sdf.parse(tenderDoc.getBidOpenTime());
            signTime = sdf.format(parse.getTime() - diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String startTime = signTime;
        map.put("startTime", signTime);
        String nowTime = DateUtil.formatLocalDateTime(LocalDateTime.now());
        map.put("nowTime", nowTime);
        if (DateTimeUtil.compareDate(nowTime, startTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == 1 && DateTimeUtil.compareDate(nowTime, endTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == -1){
            map.put("canDoIt", "true");
        } else {
            map.put("canDoIt", "false");
        }
        return map;
    }

    @Override
    public Map<String, String> getBidSectionFileUploadTimes(int bidSectionId) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        Map<String, String> map = new HashMap<>();
        String endTime = tenderDoc.getBidDocReferEndTime();
        map.put("endTime", endTime);
        String signTime = null;
        Integer signInStartTimeLeft = 1440;
        int diff = 0;
        if (signInStartTimeLeft != null) {
            diff = signInStartTimeLeft * 60 * 1000;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode());
            Date parse = sdf.parse(endTime);
            signTime = sdf.format(parse.getTime() - diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String startTime = signTime;
        map.put("startTime", signTime);
        String nowTime = DateUtil.formatLocalDateTime(LocalDateTime.now());
        map.put("nowTime", nowTime);
        if (DateTimeUtil.compareDate(nowTime, startTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == 1 && DateTimeUtil.compareDate(nowTime, endTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == -1){
            map.put("canDoIt", "true");
        } else {
            map.put("canDoIt", "false");
        }
        return map;
    }

    /**
     * 判断是否为当前标段的文件
     *
     * @param bidSectionId 标段ID
     * @param sectCode     标段编号
     * @param projType     标段类型
     * @return
     */
    private boolean judjeThisBidSection(Integer bidSectionId, String sectCode, String projType) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        boolean isSectCode = !CommonUtil.isEmpty(sectCode) && sectCode.equals(bidSection.getBidSectionCode());
        //如果是资格预审（判断是否为Epc资格预审）
        if ("EPCQualification".equals(projType)){
            projType = BidProtype.QUALIFICATION.getName();
        }

        String protypeChineseName = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode()).getName();

        boolean isProjType = !CommonUtil.isEmpty(projType) && projType.equals(protypeChineseName);
        return isProjType && isSectCode;
    }

    @Override
    public JsonData paperFileJudge(BidderOpenInfo bidderOpenInfo) {

        //设置Redis 当前已开始文件递交
        String redisKey = "BEFORE_SAVE_FILE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        RedisUtil.set(redisKey, true);

        //获取文件递交流程DTO
        List<CurrentScheduleDTO> currentScheduleS = PaperFileSchedule.listPaperCurrentSchedule();
        String scheduleKey = "CURRENT_SCHEDULE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        RedisUtil.set(scheduleKey, currentScheduleS);

        JsonData result = new JsonData();

        String yckMD5 = null;
        String syckMD5 = null;
        String yckHash = null;
        String syckHash = null;
        String czrMD5 = null;

        String certId = null;
        String cipher = null;
//        String sectCode = null;
//        String projType = null;

        //不一定上传的文件返回比对状态
        Boolean yckResult = true;
        //必须上传的文件返回比对状态
        Boolean syckResult = false;

        try {

            //判断是否在文件上传时间内
            Map<String, String> times = this.getBidSectionFileUploadTimes(bidderOpenInfo.getBidSectionId());

            if ("false".equals(times.get("canDoIt"))){
                result.setCode("2");
                result.setMsg("文件上传失败，不在文件上传时间内！");
                return result;
            }

            //解析czr获取对应hash值
            if (bidderOpenInfo.getBidderFileInfo().getCzrId() != null) {
                Fdfs fdfs = fdfsService.downloadByUpload(bidderOpenInfo.getBidderFileInfo().getCzrId());
                czrMD5 = FileUtil.getFileMD5(fdfs.getBytes());
                yckHash = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.GEF_HASH);
                syckHash = BidXmlUtil.getAttrByBytes(fdfs.getBytes(),CzrConstant.SGEF_HASH);
                certId = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.CA_NUM);
                cipher = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.CIPHER).replaceAll("\\n","").trim();
//                sectCode = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.SECT_CODE);
//                projType = BidXmlUtil.getAttrByBytes(fdfs.getBytes(), CzrConstant.PROJ_TYPE);

                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.CZR_PARSING.getIndex(), Status.PROCESSING.getCode());
            }

            //判断文件是否为当前标段文件
//            if (!judjeThisBidSection(bidderOpenInfo.getBidSectionId(), sectCode, projType)) {
//                scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.BID_CONSISTENT.getIndex(), Status.END.getCode());
//
//                result.setCode("2");
//                result.setMsg("文件上传失败，经检测您上传的文件信息与该标段信息不符合，请重新上传");
//                return result;
//            }

//            scheduleRedisSet(scheduleKey , currentScheduleS, FileSchedule.BID_CONSISTENT.getIndex(), Status.PROCESSING.getCode());
            /**
             * 逻辑分析：
             * 两者与运算为返回值
             * yck不一定上传默认为true，如果syck比对成功返回true
             * yck比对不成功，或者上传了，计算出错等都改为false
             * syck必须上传，除非比对成功，否则为false
             */
            //获取MD5值

            if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                Fdfs fdfs = fdfsService.getFdfsByUpload(bidderOpenInfo.getBidderFileInfo().getGefId());
                yckMD5 = fdfs.getFileHash();
            }
            if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                Fdfs fdfs = fdfsService.getFdfsByUpload(bidderOpenInfo.getBidderFileInfo().getSgefId());
                syckMD5 = fdfs.getFileHash();
            }

            //比对yckHash
            if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                if (yckMD5 != null && yckHash != null) {
                    yckResult = yckHash.equalsIgnoreCase(yckMD5);
                } else {
                    yckResult = false;
                }
            }
            //比对syckHash
            if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                if (syckMD5 != null && syckHash != null) {
                    syckResult = syckHash.equalsIgnoreCase(syckMD5);
                }
            }

            if (yckResult && syckResult) {
                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.FILE_SEALING.getIndex(), Status.PROCESSING.getCode());
            }else {
                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.FILE_SEALING.getIndex(), Status.END.getCode());
            }

            //数据封装
            if (yckResult && syckResult) {
                if (yckMD5 != null) {
                    bidderOpenInfo.getBidderFileInfo().setGefHash(yckMD5);
                }
                bidderOpenInfo.getBidderFileInfo().setSgefHash(syckMD5);
                bidderOpenInfo.getBidderFileInfo().setCzrHash(czrMD5);

                if (certId != null) {
                    bidderOpenInfo.getBidderFileInfo().setCertId(certId);
                }
                bidderOpenInfo.getBidderFileInfo().setCipher(cipher);
                bidderOpenInfo.setUpfileTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.DATA_ENCAPSULATION.getIndex(), Status.PROCESSING.getCode());
            }

            if (yckResult && syckResult) {
                //上传区块链
                String yckUrl = null;
                String syckUrl = null;
                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                    yckUrl = bsnChainInfoService.addBidDocumentsWitness(czrMD5, yckMD5, ".yck", bidderOpenInfo.getBidderId());
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                    syckUrl = bsnChainInfoService.addBidDocumentsWitness(czrMD5, syckMD5, ".syck", bidderOpenInfo.getBidderId());
                }
                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.DATA_UPLOAD.getIndex(), Status.PROCESSING.getCode());

                String yckQrPathtoPdf = null;
                String syckQrPathtoPdf = null;
                String yckQrPath = null;
                String syckQrPath = null;
                String qrLofoPath = FileUtil.getProjectResourcePath() + File.separator + "img" + File.separator + "qrLogo.png";
                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null) {
                    UUID yckUuid = UUID.randomUUID();
                    yckQrPathtoPdf = "img/" + bidderOpenInfo.getId() + "-" + yckUuid + ".png";
                    yckQrPath = FileUtil.getProjectResourcePath() + "/" + yckQrPathtoPdf;
                    TwoDimensionCode.encoderQRCode(yckUrl, yckQrPath, "png", 20, qrLofoPath);
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null) {
                    UUID syckUuid = UUID.randomUUID();
                    syckQrPathtoPdf = "img/" + bidderOpenInfo.getId() + "-" + syckUuid + ".png";
                    syckQrPath = FileUtil.getProjectResourcePath() + "/" + syckQrPathtoPdf;
                    TwoDimensionCode.encoderQRCode(syckUrl, syckQrPath, "png", 20, qrLofoPath);
                }

                //模板数据封装
                Map<String, Object> data = getReceiptData(bidderOpenInfo, yckQrPathtoPdf, syckQrPathtoPdf);
                //生成PDF并上传，存入ID到boi中
                Integer pdfId = createReceiptPDF(data);

                if (bidderOpenInfo.getBidderFileInfo().getGefId() != null && yckUrl != null) {
                    FileUtil.deleteFile(new File(yckQrPath));
                }
                if (bidderOpenInfo.getBidderFileInfo().getSgefId() != null && yckUrl != null) {
                    FileUtil.deleteFile(new File(syckQrPath));
                }
                bidderOpenInfo.getBidderFileInfo().setReceiptId(pdfId);

                scheduleRedisSet(scheduleKey , currentScheduleS, PaperFileSchedule.RECEIPT_SYNTHESIS.getIndex(), Status.PROCESSING.getCode());
            }
            if (yckResult && syckResult) {
                //数据存储
                updateBidderOpenInfo(bidderOpenInfo);
                Bidder bidder = new Bidder();
                bidder.setId(bidderOpenInfo.getBidderId());
                bidder.setBidDocId(bidderOpenInfo.getBidderFileInfo().getSgefId());
                bidder.setBidDocType(FileType.GEFORTJY.getType());
                bidderService.updateBidderById(bidder);
            }

            if (yckResult && syckResult){
                result.setCode("1");
            } else {
                result.setCode("2");
                result.setMsg("文件上传失败，经检测您上传的投标文件文件与投标备用文件不是同一证书下制作的，请重新上传");
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("2");
            result.setMsg("网络异常,请稍后重试!");
            return result;
        } finally {
            RedisUtil.delete(redisKey);
            RedisUtil.delete(scheduleKey);
        }
    }
}
