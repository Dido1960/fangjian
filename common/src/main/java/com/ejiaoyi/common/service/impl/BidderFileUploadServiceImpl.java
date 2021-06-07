package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.common.entity.BidderFileInfo;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.constant.UserType;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.FileReUplodType;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.quantity.PathType;
import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
import com.ejiaoyi.common.enums.quantity.QuantityUserType;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 投标文件解密后 上传接口实现
 * @Auther: liuguoqiang
 * @Date: 2021-1-5 11:36
 */
@Service
public class BidderFileUploadServiceImpl implements IBidderFileUploadService {

    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Value("${quantity.path-type}")
    private String pathType;
    @Value("${quantity.user-type}")
    private String userType;

    @Autowired
    private Environment env;

    @Override
    public void bidderFileUpload(Integer bidderId, Integer fileId, File file, boolean isConstruction) {
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);

        //构造基础Mark
        String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + fileId;
        //清单 Mark
        //String checkListMark = baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;

        //文件夹路径
        //String fileBasePath = file.getPath();

        //判断 施工 上传清单
        /*if (isConstruction) {
            ThreadUtlis.run(() -> {
                try {
                    String checkListPath = fileBasePath + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                    fdfsService.upload(new File(checkListPath), checkListMark);

                    PathType thisPathType = PathType.getEnum(pathType);

                    String url = null;
                    switch (thisPathType){
                        case NETWORK:
                            Fdfs fdfs = fdfsService.getFdfsByMark(checkListMark);
                            String outIp = env.getProperty("fdfs.address");
                            String innerIp = env.getProperty("fdfs.intranet-address");
                            if (StringUtils.isEmpty(innerIp)) {
                                innerIp = outIp;
                            }
                            url = fdfs.getUrl().replace(outIp,innerIp);
                            break;
                        case LOCAL:
                            url = checkListPath;
                            break;
                        default:
                    }
                    fdfsService.upload(uploadFile, mark);
                    fileStatusUpload(fileId, bidderFileInfo.getId(), checkListMark, Status.PROCESSING.getCode());

                    if (QuantityUserType.TEST.getCode().equals(userType)){
                        bidderFileInfoService.updateById(BidderFileInfo.builder()
                                .id(bidderFileInfo.getId())
                                .allFileStatus(Status.PROCESSING.getCode())
                                .build());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    fileStatusUpload(fileId, bidderFileInfo.getId(), checkListMark, Status.END.getCode());
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .allFileStatus(Status.END.getCode())
                            .build());
                }
            });
        }*/

        //所有 文件上传 排除清单xml
        List<File> files = FileUtil.listDirFile(file);
        for (File uploadFile : files) {
            String mark = baseMark + File.separator + uploadFile.getPath().replace(file.getPath() + File.separator, "");
            try {
                /*if (checkListMark.equals(mark)){
                    continue;
                }*/
                fdfsService.upload(uploadFile, mark);
                //判断 上传文件 是否需要标记
                fileStatusUpload(fileId, bidderFileInfo.getId(), mark, Status.PROCESSING.getCode());
            } catch (Exception e){
                e.printStackTrace();
                fileStatusUpload(fileId, bidderFileInfo.getId(), mark, Status.END.getCode());
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfo.getId())
                        .allFileStatus(Status.END.getCode())
                        .build());
            }
        }

        updateAllStatus(bidderFileInfo.getId(), isConstruction);
    }

    @Override
    public void paperBidderFileUpload(Integer bidderId, Integer fileId, File file) {
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);

        //构造基础Mark
        String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + fileId;

        //所有 文件上传
        List<File> files = FileUtil.listDirFile(file);
        for (File uploadFile : files) {
            String mark = baseMark + File.separator + uploadFile.getPath().replace(file.getPath() + File.separator, "");
            try {
                fdfsService.upload(uploadFile, mark);
                //判断 上传文件 是否需要标记
                fileStatusUpload(fileId, bidderFileInfo.getId(), mark, Status.PROCESSING.getCode());
            } catch (Exception e){
                e.printStackTrace();
                fileStatusUpload(fileId, bidderFileInfo.getId(), mark, Status.END.getCode());
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfo.getId())
                        .allFileStatus(Status.END.getCode())
                        .build());
            }
        }

        BidderFileInfo newBidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);

        if (Status.NOT_START.getCode().equals(newBidderFileInfo.getAllFileStatus())){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .allFileStatus(Status.PROCESSING.getCode())
                    .build());
        }
    }


    /**
     * 文件上传 状态更新
     * @param fileId 解密文件ID
     * @param bidderFileInfoId 投标人文件表Id
     * @param mark 上传文件Mark
     * @param status 更新状态
     */
    private void fileStatusUpload(Integer fileId, Integer bidderFileInfoId, String mark, Integer status) {
        //构造基础Mark
        String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + fileId;

        //商务文件
        String businessMark = baseMark + TenderFileConstant.BUSINESS_FILE;
        //技术文件
        String technologyMark = baseMark + TenderFileConstant.TECHNICAL_FILE;
        //资格证明
        String qualificationsMark = baseMark + TenderFileConstant.QUALIFICATION_FILE;
        //工程量清单
        String checkListPdfMark = baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF;
        //工程量清单xml
        String checkListXmlMark = baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
        //纸质标投标PDF
        String paperBidderPDFMark = baseMark + TenderFileConstant.PAPER_BID_FILE;

        if (businessMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .businessStatus(status)
                    .build());
        }else if (technologyMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .technicalStatus(status)
                    .build());
        }else if (qualificationsMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .qualificationsStatus(status)
                    .build());

        }else if (checkListPdfMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .checklistStatus(status)
                    .build());
        }else if (checkListXmlMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .checklistXmlStatus(status)
                    .build());
        }else if (paperBidderPDFMark.equals(mark)){
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfoId)
                    .businessStatus(status)
                    .build());
        }else {
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getById(bidderFileInfoId);
            if (!Status.END.equals(bidderFileInfo.getOtherStatus())){
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfoId)
                        .otherStatus(status)
                        .build());
            }
        }
    }

    @Override
    public Boolean bidderFileReUpload(Integer bidderId, Integer fileType) {
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
        boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        FileReUplodType type = FileReUplodType.getEnum(fileType);
        bidderFileInfoService.updateById(BidderFileInfo.builder()
                .id(bidderFileInfo.getId())
                .allFileStatus(Status.NOT_START.getCode())
                .build());

        //构造基础Mark
        String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();
        //解压路径
        String unzipPath = bidderFileInfo.getFileUnzipPath();

        try {
            switch (type){
                case BUSINESS:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .businessStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadFile(unzipPath + TenderFileConstant.BUSINESS_FILE, baseMark + TenderFileConstant.BUSINESS_FILE, bidderFileInfo, type, isConstruction);
                    break;
                case TECHNOLOGY:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .technicalStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadFile(unzipPath + TenderFileConstant.TECHNICAL_FILE, baseMark + TenderFileConstant.TECHNICAL_FILE, bidderFileInfo, type, isConstruction);
                    break;
                case QUALIFICATIONS:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .qualificationsStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadFile(unzipPath + TenderFileConstant.QUALIFICATION_FILE, baseMark + TenderFileConstant.QUALIFICATION_FILE, bidderFileInfo, type, isConstruction);
                    break;
                case CHECKLIST_PDF:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .checklistStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadFile(unzipPath + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF, baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF, bidderFileInfo, type, isConstruction);
                    break;
                case CHECKLIST_XML:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .checklistXmlStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadFile(unzipPath + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML, baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML, bidderFileInfo, type, isConstruction);
                    break;
                case OTHER:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfo.getId())
                            .otherStatus(Status.NOT_START.getCode())
                            .build());
                    reUploadOtherFile(unzipPath , baseMark , bidderFileInfo, isConstruction);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void reUploadFile(String path, String mark, BidderFileInfo bidderFileInfo, FileReUplodType type, boolean isConstruction){
        Integer bidderFileInfoId = bidderFileInfo.getId();
        ThreadUtlis.run(() -> {
            Integer status;
            try {
                fdfsService.upload(new File(path), mark);
                status = Status.PROCESSING.getCode();
            } catch (Exception e) {
                status = Status.END.getCode();
                e.printStackTrace();
            }

            switch (type) {
                case BUSINESS:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfoId)
                            .businessStatus(status)
                            .build());
                    updateAllStatus(bidderFileInfoId, isConstruction);
                    break;
                case TECHNOLOGY:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfoId)
                            .technicalStatus(status)
                            .build());
                    updateAllStatus(bidderFileInfoId, isConstruction);
                    break;
                case QUALIFICATIONS:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfoId)
                            .qualificationsStatus(status)
                            .build());
                    updateAllStatus(bidderFileInfoId, isConstruction);
                    break;
                case CHECKLIST_PDF:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfoId)
                            .checklistStatus(status)
                            .build());
                    updateAllStatus(bidderFileInfoId, isConstruction);
                    break;
                case CHECKLIST_XML:
                    bidderFileInfoService.updateById(BidderFileInfo.builder()
                            .id(bidderFileInfoId)
                            .checklistXmlStatus(status)
                            .build());
                    updateAllStatus(bidderFileInfoId, isConstruction);
//                    break;
                case OTHER:
                    break;
                default:
                    break;
            }
        });
    }

    private void reUploadOtherFile(String basePath, String baseMark, BidderFileInfo bidderFileInfo, boolean isConstruction){
        Integer bidderFileInfoId = bidderFileInfo.getId();
        ThreadUtlis.run(() -> {
            Integer status = Status.END.getCode();
            try {
                fdfsService.upload(new File(basePath + TenderFileConstant.TENDER_RELATE_CONTENT), baseMark + TenderFileConstant.TENDER_RELATE_CONTENT);
                fdfsService.upload(new File(basePath + TenderFileConstant.TENDER_INFO), baseMark + TenderFileConstant.TENDER_INFO);
                fdfsService.upload(new File(basePath + TenderFileConstant.JUDGING_POINT_XML), baseMark + TenderFileConstant.JUDGING_POINT_XML);
                status = Status.PROCESSING.getCode();
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfoId)
                        .otherStatus(status)
                        .build());
            } catch (IOException e) {
                e.printStackTrace();
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfoId)
                        .otherStatus(status)
                        .build());
            }

            updateAllStatus(bidderFileInfoId, isConstruction);
        });
    }

    private void updateAllStatus(Integer bidderFileInfoId, boolean isConstruction) {
        BidderFileInfo info = bidderFileInfoService.getById(bidderFileInfoId);
        if (isConstruction){
            if (Status.PROCESSING.getCode().equals(info.getBusinessStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getTechnicalStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getQualificationsStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getChecklistStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getChecklistXmlStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getOtherStatus())){

                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfoId)
                        .allFileStatus(Status.PROCESSING.getCode())
                        .build());
            }
        }else {
            if (Status.PROCESSING.getCode().equals(info.getBusinessStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getTechnicalStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getQualificationsStatus()) &&
                    Status.PROCESSING.getCode().equals(info.getOtherStatus())){

                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfoId)
                        .allFileStatus(Status.PROCESSING.getCode())
                        .build());
            }
        }

    }

    @Override
    public Boolean paperBidderFileReUpload(Integer bidderId, Integer fileType) {
        try {
            Bidder bidder = bidderService.getBidderById(bidderId);
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .allFileStatus(Status.NOT_START.getCode())
                    .build());

            //构造基础Mark
            String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();
            //解压路径
            String unzipPath = bidderFileInfo.getFileUnzipPath();
            Integer pdfFile = 1;
            Integer otherFile = 2;
            if (pdfFile.equals(fileType)){
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfo.getId())
                        .businessStatus(Status.NOT_START.getCode())
                        .build());
                ThreadUtlis.run(() -> {
                    Integer status = Status.END.getCode();
                    try {
                        fdfsService.upload(new File(unzipPath + TenderFileConstant.PAPER_BID_FILE), baseMark + TenderFileConstant.PAPER_BID_FILE);
                        status = Status.PROCESSING.getCode();
                        bidderFileInfoService.updateById(BidderFileInfo.builder()
                                .id(bidderFileInfo.getId())
                                .businessStatus(status)
                                .build());
                        BidderFileInfo info = bidderFileInfoService.getBidderFileInfoByBidderId(bidderFileInfo.getId());
                        if (Status.PROCESSING.getCode().equals(info.getBusinessStatus()) &&
                                Status.PROCESSING.getCode().equals(info.getOtherStatus())){

                            bidderFileInfoService.updateById(BidderFileInfo.builder()
                                    .id(bidderFileInfo.getId())
                                    .allFileStatus(Status.PROCESSING.getCode())
                                    .build());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        bidderFileInfoService.updateById(BidderFileInfo.builder()
                                .id(bidderFileInfo.getId())
                                .businessStatus(status)
                                .build());
                    }
                });
            }else if (otherFile.equals(fileType)){
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfo.getId())
                        .otherStatus(Status.NOT_START.getCode())
                        .build());
                ThreadUtlis.run(() -> {
                    Integer status = Status.END.getCode();
                    try {
                        fdfsService.upload(new File(unzipPath + TenderFileConstant.TENDER_RELATE_CONTENT), baseMark + TenderFileConstant.TENDER_RELATE_CONTENT);
                        fdfsService.upload(new File(unzipPath + TenderFileConstant.TENDER_INFO), baseMark + TenderFileConstant.TENDER_INFO);
                        status = Status.PROCESSING.getCode();
                        bidderFileInfoService.updateById(BidderFileInfo.builder()
                                .id(bidderFileInfo.getId())
                                .otherStatus(status)
                                .build());
                        BidderFileInfo info = bidderFileInfoService.getBidderFileInfoByBidderId(bidderFileInfo.getId());
                        if (Status.PROCESSING.getCode().equals(info.getBusinessStatus()) &&
                                Status.PROCESSING.getCode().equals(info.getOtherStatus())){

                            bidderFileInfoService.updateById(BidderFileInfo.builder()
                                    .id(bidderFileInfo.getId())
                                    .allFileStatus(Status.PROCESSING.getCode())
                                    .build());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        bidderFileInfoService.updateById(BidderFileInfo.builder()
                                .id(bidderFileInfo.getId())
                                .otherStatus(status)
                                .build());
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getTbXmlUrl(Integer bidSectionId,Bidder bidder) {
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
        //构造基础Mark
        String baseMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();
        //清单 Mark
        String checkListMark = baseMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;

        //文件夹路径
        Fdfs fdfs = fdfsService.getFdfsByMark(checkListMark);
        if (!CommonUtil.isEmpty(fdfs)){
            String outIp = env.getProperty("fdfs.address");
            String innerIp = env.getProperty("fdfs.intranet-address");
            if (StringUtils.isEmpty(innerIp)) {
                innerIp = outIp;
            }
            return fdfs.getUrl().replace(outIp,innerIp);
        }
        return null;
    }
}
