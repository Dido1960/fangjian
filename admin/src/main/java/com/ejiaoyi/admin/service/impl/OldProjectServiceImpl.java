package com.ejiaoyi.admin.service.impl;

import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.admin.dto.BidInfoDTO;
import com.ejiaoyi.admin.dto.TenderInfoDTO;
import com.ejiaoyi.admin.service.IOldProjectService;
import com.ejiaoyi.admin.util.ParseXmlUtil;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目信息 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-04-13
 */
@Service
@Slf4j
public class OldProjectServiceImpl extends BaseServiceImpl implements IOldProjectService {
    @Autowired
    private FDFSServiceImpl fdfsService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private TenderProjectServiceImpl tenderProjectService;
    @Autowired
    private BidSectionServiceImpl bidSectionService;
    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;
    @Autowired
    private TenderDocServiceImpl tenderDocService;
    @Autowired
    private BidderServiceImpl bidderService;
    @Autowired
    private BidderFileInfoServiceImpl bidderFileInfoService;
    @Autowired
    private BidderOpenInfoServiceImpl bidderOpenInfoService;

    @Override
    public JsonData addOldProject(String fileArchive, Integer regId) {
        JsonData jsonData = new JsonData();
        try {
            File file = new File(fileArchive);
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (!file1.isDirectory()) {
                    System.out.println("文件压缩层级不正确" + file1.getPath());
                    continue;
                }
                File[] files1 = file1.listFiles();
                Map<String, String> fileNames = new HashMap<>();
                for (File childFile : files1) {
                    fileNames.put(childFile.getName(), childFile.getAbsolutePath());
                }
                String tenderDocStr = fileNames.get("招标文件");
                if (StringUtils.isNotEmpty(tenderDocStr)) {
                    File tenderDoc = new File(tenderDocStr);
                    File[] tenderFiles = tenderDoc.listFiles();
                    Map<String, String> tenderFileMap = new HashMap<>();
                    for (File childFile : tenderFiles) {
                        tenderFileMap.put(childFile.getName(), childFile.getAbsolutePath());
                    }
                    BidInfoDTO bidInfoDTO = ParseXmlUtil.parseBidInfoXml(tenderFileMap.get(BidFileConstant.OldFileName.TENDER_INFO));
                    String bidProType = ParseXmlUtil.parseProTypeByXml(tenderFileMap.get(BidFileConstant.OldFileName.CONFIG_XML));
                    Integer bidSectionId = addProjectInfo(bidInfoDTO, bidProType, regId);
                    if (bidSectionId == null) {
                        System.out.println(bidInfoDTO.getBidSectionName() + "新增失败！！！！！！");
                        continue;
                    }
                    fdfsService.uploadProjectFile(ProjectFileTypeConstant.OLD_TENDER_DOC, bidSectionId, tenderDoc);
                    for (String key : fileNames.keySet()) {
                        if (!"招标文件".equals(key)) {
                            String filePath = fileNames.get(key);
                            File bidFilePath = new File(filePath);
                            File[] bidFiles = bidFilePath.listFiles();
                            Map<String, String> bidFileMap = new HashMap<>();
                            for (File childFile : bidFiles) {
                                bidFileMap.put(childFile.getName(), childFile.getAbsolutePath());
                            }
                            TenderInfoDTO tenderInfoDTO = ParseXmlUtil.parseTenderInfoXml(bidFileMap.get(TenderFileConstant.OldFileName.TENDER_INFO));
                            Integer bidderId = addBidderOpenInfo(bidSectionId, tenderInfoDTO);
                            if (bidderId == null) {
                                System.out.println(tenderInfoDTO.getBidderName() + "新增失败！！！！！！");
                                continue;
                            }
                            fdfsService.uploadProjectFile(ProjectFileTypeConstant.OLD_BIDDER_FILE, bidderId, bidFilePath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("文件新增项目异常");
            jsonData.setMsg("文件新增项目异常");
            jsonData.setCode(Enabled.NO.getCode().toString());
            return jsonData;
        }

        return null;
    }

    /***
     * 新增项目
     * @param bidInfoDTO 文件解析内容
     * @param bidClassifyCode 标段类型
     * @param regId 区划id
     * @return 标段id
     */
    private Integer addProjectInfo(BidInfoDTO bidInfoDTO, String bidClassifyCode, Integer regId) {
        // 将招标文件中信息封装到实体类中
        Project project = Project.builder()
                .projectCode(bidInfoDTO.getProjectCode())
                .projectName(bidInfoDTO.getProjectName())
                .regId(regId)
                .createTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .build();

        Integer pCount = projectService.countProjectByCode(project.getProjectCode(), project.getRegId());

        Integer pid;
        if (pCount != 0) {
            // 存在 获取pid
            pid = projectService.getProjectByCode(project.getProjectCode(), project.getRegId()).getId();
            project.setId(pid);
            projectService.updateById(project);
        } else {
            // 不存在 新增
            projectService.save(project);
            Project projectByCode = projectService.getProjectByCode(project.getProjectCode(), project.getRegId());
            pid = projectByCode.getId();
        }

        // 新增招标项目
        TenderProject tenderProject = TenderProject.builder()
                .tenderProjectName(bidInfoDTO.getProjectCode())
                .tenderProjectCode(bidInfoDTO.getProjectCode())
                .tendererName(bidInfoDTO.getTendererName())
                .tenderAgencyName(bidInfoDTO.getAgencyName())
                .regId(regId)
                .projectId(pid)
                .createTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .build();

        // 判定招标项目编号是否存在
        Integer tpCount = tenderProjectService.countTenderProjectByCode(tenderProject.getTenderProjectCode(), tenderProject.getRegId());

        Integer tid;
        TenderProject oldTenderProject = new TenderProject();
        // 招标项目存在 获取原有招标项目信息
        if (tpCount != 0) {
            oldTenderProject = tenderProjectService.getTenderProjectByCode(tenderProject.getTenderProjectCode(), tenderProject.getRegId());
        }

        if (pCount != 0 && tpCount != 0 && oldTenderProject.getProjectId().equals(pid)) {
            // 如果 招标项目编号存在 项目编号存在 且关联关系正常时 获取pid
            tid = oldTenderProject.getId();
            tenderProject.setId(tid);
            tenderProjectService.updateById(tenderProject);
        } else if ((pCount != 0 && tpCount == 0) || (pCount == 0 && tpCount == 0)) {
            // 如果 项目编号存在 招标项目编号不存在 新增招标项目
            tenderProjectService.save(tenderProject);
            tid = tenderProject.getId();
        } else {
            // 其他情况属于异常情况
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException("招标项目编号与项目编号关系异常, 项目新增失败!");
        }

        // 新增标段
        BidSection bidSection = BidSection.builder()
                .bidSectionCode(bidInfoDTO.getBidSectionCode())
                .bidSectionName(bidInfoDTO.getBidSectionName())
                .bidClassifyCode(bidClassifyCode)
                .remoteEvaluation(0)
                .bidOpenOnline(0)
                .regId(regId)
                .bidOpenTime(bidInfoDTO.getBidOpenTime())
                .dataFrom(0)
                .tenderProjectId(tid)
                .bidOpenStatus(2)
                .evalStatus(2)
                .isOldProject("1")
                .build();

        Integer bCount = bidSectionService.countBidSection(bidSection);
        if (bCount != 0) {
            // 存在 则手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException("标段编号已存在, 项目新增失败!");
        }

        bidSectionService.save(bidSection);
        Integer bidSectionId = bidSection.getId();

        // 新增或修改标段信息关系表
        BidSectionRelate bidSectionRelate = BidSectionRelate.builder()
                .bidSectionId(bidSectionId)
                .regId(bidSection.getRegId())
                .isOldProject("1")
                .build();

        BidSectionRelate old = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        if (old != null) {
            bidSectionRelate.setAwayEvalSite(null);
            bidSectionRelate.setAwayRegId(null);
            bidSectionRelate.setHomeEvalSite(null);
            bidSectionRelate.setHomeOpenSite(null);
            bidSectionRelate.setId(old.getId());
            bidSectionRelateService.updateById(bidSectionRelate);
        } else {
            bidSectionRelateService.save(bidSectionRelate);
        }

        // 新增招标文件信息
        tenderDocService.save(TenderDoc.builder()
                .expertCount(Integer.valueOf(bidInfoDTO.getExpertCount()))
                .expertCount(Integer.valueOf(bidInfoDTO.getRepresentativeCount()))
                .bidOpenTime(bidInfoDTO.getBidOpenTime())
                .bidDocReferEndTime(bidInfoDTO.getBidOpenTime())
                .bidSectionId(bidSectionId)
                .submitTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .version(1)
                .build());

        return bidSectionId;
    }

    /**
     * 新增投标人
     * @param bidSectionId 标段id
     * @param tenderInfoDTO 文件解析内容
     * @return
     * @throws Exception
     */
    public Integer addBidderOpenInfo(Integer bidSectionId, TenderInfoDTO tenderInfoDTO) {
        List<Bidder> bidders = bidderService.getBidder(tenderInfoDTO.getBidderCode(), bidSectionId);
        if (bidders.size() == 0) {
            Integer bidderId = bidderService.saveBidder(Bidder.builder()
                    .bidderName(tenderInfoDTO.getBidderName())
                    .bidderOrgCode(tenderInfoDTO.getBidderCode())
                    .bidSectionId(bidSectionId)
                    .build());
            if (bidderId != null) {
                BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                        .bidSectionId(bidSectionId)
                        .bidderId(bidderId)
                        .clientName(tenderInfoDTO.getLegalAgent())
                        .bidPrice(tenderInfoDTO.getPriceTotal())
                        .timeLimit(tenderInfoDTO.getConstructionDays())
                        .rate(tenderInfoDTO.getRate())
                        .quality(tenderInfoDTO.getQuality())
                        .build();
                // 新增
                BidderFileInfo bidderFileInfo = BidderFileInfo.builder()
                        .bidderId(bidderId)
                        .build();
                BidderFileInfo initBidderFile = bidderFileInfoService.initBidderFileInfo(bidderFileInfo);
                boolean isJoinBid = bidderOpenInfoService.insert(bidderOpenInfo) > 0 && initBidderFile != null;
                if (!isJoinBid) {
                    return null;
                }
                return bidderId;
            }
            return null;
        } else {
            log.error(tenderInfoDTO.getBidderName() + "已存在！！！！！！");
            return null;
        }
    }
}
