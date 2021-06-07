package com.ejiaoyi.admin.service.impl;

import com.ejiaoyi.admin.service.IProjectInfoService;
import com.ejiaoyi.common.dto.ProjectInfoTemp;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidSectionRelate;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目信息 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-01-13
 */
@Service
@Slf4j
public class ProjectInfoServiceImpl extends BaseServiceImpl implements IProjectInfoService {
    @Autowired
    private BidSectionServiceImpl bidSectionService;
    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;
    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Override
    public Boolean updateProjectInfo(ProjectInfoTemp projectInfoTemp) {
        Integer bidSectionId = projectInfoTemp.getBidSectionId();
        Integer tenderDocId = projectInfoTemp.getTenderDocId();
        boolean a = bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .regId(projectInfoTemp.getRegId())
                .remoteEvaluation(projectInfoTemp.getRemoteEvaluation())
                .bidOpenOnline(projectInfoTemp.getBidOpenOnline())
                .build()) == 1;
        Boolean b = bidSectionRelateService.updateRelateBySectionId(BidSectionRelate.builder()
                .bidSectionId(bidSectionId)
                .regId(projectInfoTemp.getRegId())
                .build());
        boolean c = tenderDocService.updateTenderDocById(TenderDoc.builder()
                .id(tenderDocId)
                .bidOpenTime(projectInfoTemp.getBidOpenTime())
                .bidDocReferEndTime(projectInfoTemp.getBidDocReferEndTime())
                .expertCount(projectInfoTemp.getExpertCount())
                .representativeCount(projectInfoTemp.getRepresentativeCount())
                .build());
        return a && b && c;
    }
}
