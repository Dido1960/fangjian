package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.BidderFileUploadDTO;
import com.ejiaoyi.common.dto.BidderReviewPointDTO;
import com.ejiaoyi.common.dto.DownBidderFileDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.mapper.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 投标文件信息 服务实现类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-30
 */
@Service
public class BidderFileInfoServiceImpl extends BaseServiceImpl<BidderFileInfo> implements IBidderFileInfoService {

    @Autowired
    private BidderFileInfoMapper bidderFileInfoMapper;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IDownloadService downloadService;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private BidderMapper bidderMapper;

    @Resource
    private EvalResultSgMapper evalResultSgMapper;

    @Resource
    private EvalResultJlMapper evalResultJlMapper;

    @Resource
    private  EvalResultEpcMapper evalResultEpcMapper;

    @Resource
    private ICandidateSuccessService candidateSuccessService;

    @Override
    @UserLog(value = "'投标文件评审点解析:投标人id：'+#bidderId", dmlType = DMLType.INSERT)
    @Cacheable(value = CacheName.REVIEW_POINT, key = "#bidderId", unless = "#result==null")
    public List<BidderReviewPointDTO> saveBidderReviewPoint(String reviewPoint, Integer bidderId, Integer bidSectionId, boolean downStatus) {
        List<BidderReviewPointDTO> bidderReviewPointDTOS;
        FileInputStream fis = null;
        try {
            if (downStatus) {
                Bidder bidder = bidderService.getBidderById(bidderId);
                String mark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId() + TenderFileConstant.JUDGING_POINT_XML;
                // fdfs路径
                String path = fdfsService.getUrlByMark(mark);
                if (StringUtils.isEmpty(path)) {
                    return null;
                }
                try {
                    byte[] bytes = fdfsService.downloadByUrl(path);
                    FileUtil.writeFile(bytes, reviewPoint);
                    downloadService.multiThreadDownload(path, reviewPoint, 1);
                } catch (Exception e) {
                    throw new CustomException("评审点下载失败！");
                }
            }
            if (!new File(reviewPoint).exists()) {
                return null;
            }
            fis = new FileInputStream(reviewPoint);
            SAXReader reader = new SAXReader();
            Document ducument = reader.read(fis);
            Element root = ducument.getRootElement();
            Element method = root.element("BidEvaluationMethod");

            if (method == null) {
                return null;
            }

            List<Element> gradeEles = method.elements("EvaluationFactor");
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            List<Grade> grades = gradeService.listGrade(tenderDoc.getGradeId().split(","), null);

            bidderReviewPointDTOS = new ArrayList<>();
            for (int i = 0; i < gradeEles.size(); i++) {
                Element gradeEle = gradeEles.get(i);
                Grade grade = grades.get(i);

                List<Element> gradeItemEles = gradeEle.elements("EvaluationStandard");
                for (int j = 0; j < gradeItemEles.size(); j++) {
                    BidderReviewPointDTO bidderReviewPointDTO = BidderReviewPointDTO.builder()
                            .bidderId(bidderId)
                            .build();
                    bidderReviewPointDTO.setGradeId(grade.getId());
                    Element gradeItemEle = gradeItemEles.get(j);
                    GradeItem gradeItem = grade.getGradeItems().get(j);

                    bidderReviewPointDTO.setGradeItemId(gradeItem.getId());
                    String bsReviewPointPageInfo = gradeItemEle.attributeValue("bsReviewPointPageInfo");
                    String teReviewPointPageInfo = gradeItemEle.attributeValue("teReviewPointPageInfo");
                    String quReviewPointPageInfo = gradeItemEle.attributeValue("quReviewPointPageInfo");
                    if (StringUtils.isNotEmpty(bsReviewPointPageInfo)) {
                        String[] split = bsReviewPointPageInfo.split(";");
                        List<String> bsReviewPointPages = Arrays.asList(split);
                        bidderReviewPointDTO.setBsReviewPointPages(bsReviewPointPages);
                    }
                    if (StringUtils.isNotEmpty(teReviewPointPageInfo)) {
                        String[] split = teReviewPointPageInfo.split(";");
                        List<String> teReviewPointPages = Arrays.asList(split);
                        bidderReviewPointDTO.setTeReviewPointPages(teReviewPointPages);
                    }
                    if (StringUtils.isNotEmpty(quReviewPointPageInfo)) {
                        String[] split = quReviewPointPageInfo.split(";");
                        List<String> quReviewPointPages = Arrays.asList(split);
                        bidderReviewPointDTO.setQuReviewPointPages(quReviewPointPages);
                    }
                    bidderReviewPointDTOS.add(bidderReviewPointDTO);
                }
            }
            return bidderReviewPointDTOS;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("bidder:" + bidderId + " review point parse exception: " + e.getMessage());
            return null;
        } finally {
            if (downStatus) {
                FileUtil.deleteFile(reviewPoint);
            }
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Cacheable(value = CacheName.BIDDER_FILE_INFO, key = "'bidder_' + #bidderId", unless = "#result==null")
    public BidderFileInfo getBidderFileInfoByBidderId(Integer bidderId) {
        Assert.notNull(bidderId, "param bidderId can not be null!");
        QueryWrapper<BidderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderId);
        return bidderFileInfoMapper.selectOne(queryWrapper);
    }

    @Override
    @RedissonLock(key = "#bidderFileInfo.bidderId + '_bidderFileInit'")
    @CacheEvict(value = CacheName.BIDDER_FILE_INFO, allEntries = true)
    public BidderFileInfo initBidderFileInfo(BidderFileInfo bidderFileInfo) {
        Assert.notNull(bidderFileInfo, "param bidderFileInfo can not be null!");
        Assert.notNull(bidderFileInfo.getBidderId(), "param bidderId can not be null!");
        QueryWrapper<BidderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderFileInfo.getBidderId());
        BidderFileInfo result = bidderFileInfoMapper.selectOne(queryWrapper);
        if (CommonUtil.isEmpty(result)) {
            bidderFileInfoMapper.insert(bidderFileInfo);
            result = bidderFileInfo;
        }
        return result;
    }

    @Override
    @CacheEvict(value = CacheName.BIDDER_FILE_INFO, allEntries = true)
    public Integer updateById(BidderFileInfo bidderFileInfo) {
        Assert.notNull(bidderFileInfo, "param bidderFileInfo can not be null!");
        Assert.notNull(bidderFileInfo.getId(), "param id can not be null!");
        return bidderFileInfoMapper.updateById(bidderFileInfo);
    }

    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownBidderFileDTO(Integer bidSectionId) {
        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        // 通过开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        for (Bidder bidder : bidders) {
            DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                    .bidderId(bidder.getId())
                    .bidderName(bidder.getBidderName())
                    .build();
            BidderFileInfo bidderFileInfo = this.getBidderFileInfoByBidderId(bidder.getId());
            if (bidderFileInfo != null) {
                // gef（对应的是upFiles的id）
                Integer gefId = bidderFileInfo.getGefId();
                // sgef上传id（对应的是upFiles的id）
                Integer sgefId = bidderFileInfo.getSgefId();
                // czr上传id（对应的是upFiles的id）
                Integer czrId = bidderFileInfo.getCzrId();
                // gef文件
                if (gefId != null) {
                    Fdfs gefFdfs = fdfsService.getFdfsByUpload(gefId);
                    downBidderFileDTO.setGefFdfs(gefFdfs);
                }
                // sgef文件
                if (sgefId != null) {
                    Fdfs sgefFdfs = fdfsService.getFdfsByUpload(sgefId);
                    downBidderFileDTO.setSgefFdfs(sgefFdfs);
                }
                // czr文件
                if (czrId != null) {
                    Fdfs czrFdfs = fdfsService.getFdfsByUpload(czrId);
                    downBidderFileDTO.setCzrFdfs(czrFdfs);
                }
            }

            // pdf文件
            if (bidder.getBidDocId() != null) {
                String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

                // 纸质标只有一个投标文件pdf
                if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                    Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                    downBidderFileDTO.setPaperBidderPdf(paperPdf);
                } else {
                    Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                    downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                    Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                    downBidderFileDTO.setBusinessFilePdf(busPdf);

                    Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                    downBidderFileDTO.setQualificationFilePdf(quaPdf);
                    // 施工才有工程量清单pdf
                    if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                        Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                        downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                    }
                }
            }
            downBidderFileDTOS.add(downBidderFileDTO);
        }

        return downBidderFileDTOS;
    }

    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownAllBiddersPdfFileDTO(Integer bidSectionId) {
        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        //获取所有通过开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        for (Bidder bidder : bidders) {
            DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                    .bidderId(bidder.getId())
                    .bidderName(bidder.getBidderName())
                    .build();
            // pdf文件
            if (bidder.getBidDocId() != null) {
                String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

                // 纸质标只有一个投标文件pdf
                if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                    Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                    downBidderFileDTO.setPaperBidderPdf(paperPdf);
                } else {
                    Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                    downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                    Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                    downBidderFileDTO.setBusinessFilePdf(busPdf);

                    Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                    downBidderFileDTO.setQualificationFilePdf(quaPdf);
                    // 施工才有工程量清单pdf
                    if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                        Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                        downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                    }
                }
            }
            downBidderFileDTOS.add(downBidderFileDTO);
        }
        return downBidderFileDTOS;
    }



    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO1(Integer bidSectionId) {

        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        //通过标段ID获取 投标人施工排名数据 升序
        List<EvalResultSg> evalResultSgsList = evalResultSgMapper.listRankingBidderByBsId(bidSectionId);
        Integer bidderId =null;
        //获取第一名
        if(!evalResultSgsList.isEmpty()){
            bidderId = evalResultSgsList.get(0).getBidderId();
        }

        QueryWrapper<Bidder> query = new QueryWrapper<>();
        query.eq("ID", bidderId);
        Bidder bidder = bidderMapper.selectOne(query);

        BidSection bidSection = BidSection.builder().id(bidder.getBidSectionId()).build();

        DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                .bidderId(bidderId)
                .bidderName(bidder.getBidderName())
                .build();
        // pdf文件
        if (bidder.getBidDocId() != null) {
            String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

            // 纸质标只有一个投标文件pdf
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                downBidderFileDTO.setPaperBidderPdf(paperPdf);
            } else {
                Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                downBidderFileDTO.setBusinessFilePdf(busPdf);

                Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                downBidderFileDTO.setQualificationFilePdf(quaPdf);
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                    downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                }
            }
        }
        downBidderFileDTOS.add(downBidderFileDTO);
        return downBidderFileDTOS;
    }


    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO2(Integer bidSectionId) {
        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        //通过标段ID获取 投标人监理排名数据 升序
        List<EvalResultJl> evalResultJlsList = evalResultJlMapper.listRankingBidderByBsId(bidSectionId);
        Integer bidderId =null;
        //获取第一名
        if(!evalResultJlsList.isEmpty()){
            bidderId = evalResultJlsList.get(0).getBidderId();
        }

        QueryWrapper<Bidder> query = new QueryWrapper<>();
        query.eq("ID", bidderId);
        Bidder bidder = bidderMapper.selectOne(query);

        BidSection bidSection = BidSection.builder().id(bidder.getBidSectionId()).build();

        DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                .bidderId(bidderId)
                .bidderName(bidder.getBidderName())
                .build();
        // pdf文件
        if (bidder.getBidDocId() != null) {
            String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

            // 纸质标只有一个投标文件pdf
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                downBidderFileDTO.setPaperBidderPdf(paperPdf);
            } else {
                Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                downBidderFileDTO.setBusinessFilePdf(busPdf);

                Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                downBidderFileDTO.setQualificationFilePdf(quaPdf);
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                    downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                }
            }
        }
        downBidderFileDTOS.add(downBidderFileDTO);
        return downBidderFileDTOS;
    }



    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO3(Integer bidSectionId) {
        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();
        //通过标段ID获取 投标人总承包排名数据 升序
        List<EvalResultEpc> evalResultEpcsList = evalResultEpcMapper.listRankingBidderByBsId(bidSectionId);
        Integer bidderId =null;
        //获取第一名
        if(!evalResultEpcsList.isEmpty()){
            bidderId = evalResultEpcsList.get(0).getBidderId();
        }

        QueryWrapper<Bidder> query = new QueryWrapper<>();
        query.eq("ID", bidderId);
        Bidder bidder = bidderMapper.selectOne(query);

        BidSection bidSection = BidSection.builder().id(bidder.getBidSectionId()).build();

        DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO.builder()
                .bidderId(bidderId)
                .bidderName(bidder.getBidderName())
                .build();
        // pdf文件
        if (bidder.getBidDocId() != null) {
            String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

            // 纸质标只有一个投标文件pdf
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                downBidderFileDTO.setPaperBidderPdf(paperPdf);
            } else {
                Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                downBidderFileDTO.setBusinessFilePdf(busPdf);

                Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                downBidderFileDTO.setQualificationFilePdf(quaPdf);
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                    downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                }
            }
        }
        downBidderFileDTOS.add(downBidderFileDTO);
        return downBidderFileDTOS;
    }


    @Override
    @Cacheable(value = CacheName.DOWN_BIDDER_FILE, key = "#bidSectionId", unless = "#result==null")
    public List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO4(Integer bidSectionId) {

        List<DownBidderFileDTO> downBidderFileDTOS = new ArrayList<>();

        //查询第一名投标人
        CandidateSuccess firstCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.FIRST_PLACE);
        Integer bidderId = firstCs.getBidderId();
        QueryWrapper<Bidder> query = new QueryWrapper<>();
        query.eq("ID", bidderId);
        Bidder bidder = bidderMapper.selectOne(query);
        //得到标段
        BidSection bidSection = BidSection.builder().id(bidder.getBidSectionId()).build();
        //封装文件下载
        DownBidderFileDTO downBidderFileDTO = DownBidderFileDTO
                .builder()
                .bidderId(bidderId)
                .bidderName(bidder.getBidderName())
                .build();

        // pdf文件
        if (bidder.getBidDocId() != null) {
            String pdfMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidder.getBidDocId();

            // 纸质标只有一个投标文件pdf
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                Fdfs paperPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.PAPER_BID_FILE);
                downBidderFileDTO.setPaperBidderPdf(paperPdf);
            } else {
                Fdfs tecPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.TECHNICAL_FILE);
                downBidderFileDTO.setTechnicalFilePdf(tecPdf);

                Fdfs busPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.BUSINESS_FILE);
                downBidderFileDTO.setBusinessFilePdf(busPdf);

                Fdfs quaPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.QUALIFICATION_FILE);
                downBidderFileDTO.setQualificationFilePdf(quaPdf);
                // 施工才有工程量清单pdf
                if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                    Fdfs engPdf = fdfsService.getFdfsByMark(pdfMark + TenderFileConstant.ENGINEER_QUANTITY_LIST_PDF);
                    downBidderFileDTO.setEngineerQuantityListPdf(engPdf);
                }
            }
        }
        downBidderFileDTOS.add(downBidderFileDTO);
        return downBidderFileDTOS;
    }

    @Override
    public Bidder getBidderByXmlUid(String xmlUid) {
        Assert.notNull(xmlUid, "param xmlUid can not be null!");
        QueryWrapper<BidderFileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("XML_UID", xmlUid);
        BidderFileInfo bidderFileInfo = bidderFileInfoMapper.selectOne(queryWrapper);
        Bidder bidder = null;
        if (bidderFileInfo != null) {
            bidder = bidderService.getBidderById(bidderFileInfo.getBidderId());
            bidder.setBidderFileInfo(bidderFileInfo);
        }
        return bidder;
    }

    @Override
    @Cacheable(value = CacheName.BIDDER_FILE_INFO, key = "'bidSection_' + #bidSectionId", unless = "#result==null")
    public BidderFileUploadDTO listBidderFileUpload(Integer bidSectionId) {
        // 通过开标的投标人
        List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId,false);

        if (CommonUtil.isEmpty(bidders)){
            return null;
        }
        BidderFileUploadDTO result = new BidderFileUploadDTO();
        result.setBidderNum(bidders.size());

        List<BidderFileInfo> list = new ArrayList<>();
        Integer success = 0;
        for (Bidder bidder : bidders) {
            BidderFileInfo bidderFileInfo = this.getBidderFileInfoByBidderId(bidder.getId());

            bidderFileInfo.setBidderName(bidder.getBidderName());

            if (Status.PROCESSING.getCode().equals(bidderFileInfo.getAllFileStatus())){
                success++;
            }

            list.add(bidderFileInfo);
        }

        result.setUploadSuccessNum(success);
        result.setBidderFileInfoList(list);

        return result;
    }

    @Override
    @Cacheable(value = CacheName.BIDDER_FILE_INFO, key = "'id_' + #id", unless = "#result==null")
    public BidderFileInfo getById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        return bidderFileInfoMapper.selectById(id);
    }

    @Override
    public List<BidderFileInfo> listXmlNoParseCompleteInfo() {
        return bidderFileInfoMapper.listXmlNoParseCompleteInfo();
    }
}
