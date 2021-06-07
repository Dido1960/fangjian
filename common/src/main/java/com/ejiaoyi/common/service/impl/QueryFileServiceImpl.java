package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.common.dto.DownBidderFileDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件查询 实现类
 *
 * @author Make
 * @since 2020-11-16
 */
@Service
public class QueryFileServiceImpl implements IQueryFileService {
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownBidderFileDTO(Integer bidSectionId) {
        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        // 进入评标系统的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        if (Enabled.YES.getCode().toString().equals(bidSectionRelate.getIsOldProject())){
            for (Bidder bidder : bidders) {
                DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                        .bidderId(bidder.getId())
                        .bidderName(bidder.getBidderName())
                        .build();
                String bidderMarkPrefix = File.separator + ProjectFileTypeConstant.OLD_BIDDER_FILE + File.separator + bidder.getId() + File.separator;

                // 商务标
                String businessPdfMark = bidderMarkPrefix + TenderFileConstant.OldFileName.BUSINESS_FILE;
                String businessPdfSigarMark = bidderMarkPrefix + TenderFileConstant.OldFileName.BUSINESS_FILE_SIGAR;
                // 技术标
                String skillPdfMark = bidderMarkPrefix + TenderFileConstant.OldFileName.TECHNICAL_FILE;
                String skillPdfSigarMark = bidderMarkPrefix + TenderFileConstant.OldFileName.TECHNICAL_FILE_SIGAR;
                // 资格证明
                String qualificationPdfMark = bidderMarkPrefix + TenderFileConstant.OldFileName.QUALIFICATION_FILE;
                String qualificationPdfSigarMark = bidderMarkPrefix + TenderFileConstant.OldFileName.QUALIFICATION_FILE_SIGAR;

                Fdfs businessPdfSigar = fdfsService.getFdfsByMark(businessPdfSigarMark);
                Fdfs skillPdfSigar = fdfsService.getFdfsByMark(skillPdfSigarMark);
                Fdfs qualificationPdfSigar = fdfsService.getFdfsByMark(qualificationPdfSigarMark);

                downBidderFileDTO.setBusinessFilePdf(businessPdfSigar != null ? businessPdfSigar : fdfsService.getFdfsByMark(businessPdfMark));
                downBidderFileDTO.setTechnicalFilePdf(skillPdfSigar != null ? skillPdfSigar : fdfsService.getFdfsByMark(skillPdfMark));
                downBidderFileDTO.setQualificationFilePdf(qualificationPdfSigar != null ? qualificationPdfSigar : fdfsService.getFdfsByMark(qualificationPdfMark));
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    // 工程量清单PDF
                    String quantityListPdfMark = bidderMarkPrefix + TenderFileConstant.OldFileName.ENGINEER_QUANTITY_LIST_PDF;
                    String quantityListPdfSigarMark = bidderMarkPrefix + TenderFileConstant.OldFileName.ENGINEER_QUANTITY_LIST_PDF_SIGAR;

                    Fdfs quantityListPdfSigar = fdfsService.getFdfsByMark(quantityListPdfSigarMark);
                    downBidderFileDTO.setEngineerQuantityListPdf(quantityListPdfSigar != null ? quantityListPdfSigar : fdfsService.getFdfsByMark(quantityListPdfMark));

                    // 工程量清单XML
                    String quantityListXmlMark = bidderMarkPrefix + TenderFileConstant.OldFileName.ENGINEER_QUANTITY_LIST_XML;
                    downBidderFileDTO.setEngineerQuantityListXML(fdfsService.getFdfsByMark(quantityListXmlMark));
                }
                downBidderFileDTOS.add(downBidderFileDTO);
            }
        } else {
            for (Bidder bidder : bidders) {
                DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                        .bidderId(bidder.getId())
                        .bidderName(bidder.getBidderName())
                        .build();
                BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
                if (bidderFileInfo != null) {
                    // gef上传id（对应的是upFiles的id）
                    Integer gefId = bidderFileInfo.getGefId();
                    // sgef上传id（对应的是upFiles的id）
                    Integer sgefId = bidderFileInfo.getSgefId();
                    // czr上传id（对应的是upFiles的id）
                    Integer czrId = bidderFileInfo.getCzrId();
                    if (gefId != null) {
                        Fdfs gefFdfs = fdfsService.getFdfsByUpload(gefId);
                        downBidderFileDTO.setGefFdfs(gefFdfs);
                    }
                    if (sgefId != null) {
                        Fdfs sgefFile = fdfsService.getFdfsByUpload(sgefId);
                        downBidderFileDTO.setSgefFdfs(sgefFile);
                    }
                    // czr文件
                    if (czrId != null) {
                        Fdfs czrFdfs = fdfsService.getFdfsByUpload(czrId);
                        downBidderFileDTO.setCzrFdfs(czrFdfs);
                    }
                }

                if (bidder.getBidDocId() != null) {
                    String bidFileUrl = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

                    // 纸质标只有一个投标文件pdf
                    if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                        Fdfs paperPdf = fdfsService.getFdfsByMark(bidFileUrl + TenderFileConstant.PAPER_BID_FILE);
                        downBidderFileDTO.setPaperBidderPdf(paperPdf);
                    } else {
                        // 商务标
                        String businessPdfMark = bidFileUrl + TenderFileConstant.BUSINESS_FILE;
                        // 技术标
                        String skillPdfMark = bidFileUrl + TenderFileConstant.TECHNICAL_FILE;
                        // 资格证明
                        String qualificationPdfMark = bidFileUrl + TenderFileConstant.QUALIFICATION_FILE;

                        downBidderFileDTO.setBusinessFilePdf(fdfsService.getFdfsByMark(businessPdfMark));
                        downBidderFileDTO.setTechnicalFilePdf(fdfsService.getFdfsByMark(skillPdfMark));
                        downBidderFileDTO.setQualificationFilePdf(fdfsService.getFdfsByMark(qualificationPdfMark));
                        // 施工才有工程量清单pdf
                        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                            // 工程量清单PDF
                            String quantityListPdfMark = bidFileUrl + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF;
                            downBidderFileDTO.setEngineerQuantityListPdf(fdfsService.getFdfsByMark(quantityListPdfMark));

                            // 工程量清单XML
                            String quantityListXmlMark = bidFileUrl + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                            downBidderFileDTO.setEngineerQuantityListXML(fdfsService.getFdfsByMark(quantityListXmlMark));
                        }
                    }
                }
                downBidderFileDTOS.add(downBidderFileDTO);
            }
        }
        return downBidderFileDTOS;
    }

    @Override
    public DownBidderFileDTO getBidderPdfFileByBidderId(Integer bidSectionId, Integer bidderId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        DownBidderFileDTO bidderFileDTO = new DownBidderFileDTO();
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
        if (bidderFileInfo != null) {
            // gef上传id（对应的是upFiles的id）
            Integer gefId = bidderFileInfo.getGefId();
            // sgef上传id（对应的是upFiles的id）
            Integer sgefId = bidderFileInfo.getSgefId();
            // czr上传id（对应的是upFiles的id）
            Integer czrId = bidderFileInfo.getCzrId();
            if (gefId != null) {
                Fdfs gefFdfs = fdfsService.getFdfsByUpload(gefId);
                bidderFileDTO.setGefFdfs(gefFdfs);
            }
            if (sgefId != null) {
                Fdfs sgefFile = fdfsService.getFdfsByUpload(sgefId);
                bidderFileDTO.setSgefFdfs(sgefFile);
            }
            // czr文件
            if (czrId != null) {
                Fdfs czrFdfs = fdfsService.getFdfsByUpload(czrId);
                bidderFileDTO.setCzrFdfs(czrFdfs);
            }
        }

        if (!CommonUtil.isEmpty(bidder.getBidDocId())) {
            String bidFileUrl = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())){
                String bidderPdfMark = bidFileUrl + TenderFileConstant.PAPER_BID_FILE;
                bidderFileDTO.setPaperBidderPdf(fdfsService.getFdfsByMark(bidderPdfMark));
                return bidderFileDTO;
            }

            // 纸质标只有一个投标文件pdf
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                Fdfs paperPdf = fdfsService.getFdfsByMark(bidFileUrl + TenderFileConstant.PAPER_BID_FILE);
                bidderFileDTO.setPaperBidderPdf(paperPdf);
            } else {
                // 商务标
                String businessPdfMark = bidFileUrl + TenderFileConstant.BUSINESS_FILE;
                // 技术标
                String skillPdfMark = bidFileUrl + TenderFileConstant.TECHNICAL_FILE;
                // 资格证明
                String qualificationPdfMark = bidFileUrl + TenderFileConstant.QUALIFICATION_FILE;

                bidderFileDTO.setBusinessFilePdf(fdfsService.getFdfsByMark(businessPdfMark));
                bidderFileDTO.setTechnicalFilePdf(fdfsService.getFdfsByMark(skillPdfMark));
                bidderFileDTO.setQualificationFilePdf(fdfsService.getFdfsByMark(qualificationPdfMark));
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    // 工程量清单PDF
                    String quantityListPdfMark = bidFileUrl + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF;
                    bidderFileDTO.setEngineerQuantityListPdf(fdfsService.getFdfsByMark(quantityListPdfMark));

                    // 工程量清单XML
                    String quantityListXmlMark = bidFileUrl + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                    bidderFileDTO.setEngineerQuantityListXML(fdfsService.getFdfsByMark(quantityListXmlMark));
                }
            }

            return bidderFileDTO;
        }

        return null;
    }

}
