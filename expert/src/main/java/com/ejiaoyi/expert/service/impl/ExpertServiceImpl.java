package com.ejiaoyi.expert.service.impl;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.ProcessCompletionDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.enums.FileType;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IGradeService;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.service.IExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-07
 */
@Service
public class ExpertServiceImpl extends BaseServiceImpl implements IExpertService {

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Override
    public boolean isGroupCompletion(String[] gradeIds, Integer evalProcess){
        ProcessCompletionDTO dto = gradeService.getProcessCompletion(gradeIds, evalProcess);
        if (dto == null) {
            return false;
        }
        return dto.getCompleteNum() != null && dto.getCompleteNum() != 0 && (dto.getNoCompleteNum() == null || dto.getNoCompleteNum() == 0);
    }

    @Override
    public List<Fdfs> listRecordTable(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Fdfs> recordFdfss = new ArrayList<>();
        String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD +  "." + FileType.PDF.getSuffix();
        if (!CommonUtil.isEmpty(bidSection.getBidOpenStatus())) {
            if (bidSection.getBidOpenStatus().equals(Status.END.getCode())) {
                Fdfs fdfs = fdfsService.getFdfsByMark(mark);
                recordFdfss.add(fdfs);
            }
        }

        return recordFdfss;
    }

    @Override
    public List<ExpertUser> listLinkNoFinishExpertUsers(List<ExpertUser> expertUsers, Integer gradeId) {
        List<ExpertUser> list = new ArrayList<>();
        for (ExpertUser expertUser : expertUsers) {
            ExpertReview expertReview = expertReviewService.getExpertReview(ExpertReview.builder()
                    .bidSectionId(expertUser.getBidSectionId())
                    .expertId(expertUser.getId())
                    .gradeId(gradeId)
                    .enabled(1)
                    .build());
            if (CommonUtil.isEmpty(expertReview)){
                list.add(expertUser);
            }
        }
        return list;
    }


}
