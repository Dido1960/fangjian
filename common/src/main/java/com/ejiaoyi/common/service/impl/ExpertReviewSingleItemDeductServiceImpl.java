package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ConDetailedMethod;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewSingleItemDeductMapper;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.service.IExpertReviewSingleItemDeductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IGradeItemService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 扣分制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ExpertReviewSingleItemDeductServiceImpl extends ServiceImpl<ExpertReviewSingleItemDeductMapper, ExpertReviewSingleItemDeduct> implements IExpertReviewSingleItemDeductService {

    @Autowired
    private ExpertReviewSingleItemDeductMapper expertReviewSingleItemDeductMapper;
    @Autowired
    private IGradeItemService gradeItemService;
    @Autowired
    private IExpertReviewService expertReviewService;

    @Override
    public List<ExpertReviewSingleItemDeduct> listExpertReviewSingleItemDeduct(ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct) {
        List<ExpertReviewSingleItemDeduct> list = expertReviewSingleItemDeductMapper.listExpertReviewSingleItemDeduct(expertReviewSingleItemDeduct);
        for (ExpertReviewSingleItemDeduct deduct : list) {
            GradeItem gradeItem = gradeItemService.getById(deduct.getGradeItemId());
            deduct.setGradeItem(gradeItem);
        }
        return list;
    }

    @Override
    @RedissonLock(key = "#expertId + '_' + #currentGrade.id")
    public ExpertReview initReviewItemDeduct(Integer bidSectionId, Integer expertId, Grade currentGrade, List<Bidder> bidders) {
        ExpertReview query = ExpertReview.builder()
                .gradeId(currentGrade.getId())
                .bidSectionId(bidSectionId)
                .expertId(expertId)
                .build();
        ExpertReview review = expertReviewService.getExpertReview(query);
        if (!CommonUtil.isEmpty(review)) {
            return review;
        }
        ExpertReview expertReview = ExpertReview.builder()
                .gradeId(currentGrade.getId())
                .initStatus(1)
                .bidSectionId(bidSectionId)
                .expertId(expertId)
                .startTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .build();
        expertReviewService.insertExpertReview(expertReview);

        ExpertReviewSingleItemDeduct deduct = ExpertReviewSingleItemDeduct.builder()
                .expertId(expertId)
                .expertReviewId(expertReview.getId())
                .gradeId(currentGrade.getId())
                .build();

        for (Bidder bidder : bidders) {
            for (GradeItem gradeItem : currentGrade.getGradeItems()) {
                deduct.setId(null);
                deduct.setBidderId(bidder.getId());
                deduct.setGradeItemId(gradeItem.getId());
                expertReviewSingleItemDeductMapper.insert(deduct);
            }
        }
        return expertReviewService.getExpertReview(query);
    }

    @Override
    public List<ExpertReviewSingleItemDeduct> listHasScore(Integer expertId, Integer gradeId, Integer bidderId) {
        return expertReviewSingleItemDeductMapper.listHasScore(expertId, gradeId, bidderId);
    }

    @Override
    public Integer updateExpertReviewSingleItemDeduct(ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct) {
        Assert.notNull(expertReviewSingleItemDeduct.getId(), "param id can not be null!");
        return expertReviewSingleItemDeductMapper.updateById(expertReviewSingleItemDeduct);
    }

    @Override
    public Integer updateListDeduct(Integer userId, Integer gradeId) {
        UpdateWrapper<ExpertReviewSingleItemDeduct> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("GRADE_ID", gradeId);
        updateWrapper.eq("EXPERT_ID", userId);
        updateWrapper.set("EVAL_RESULT", 1);
        return expertReviewSingleItemDeductMapper.update(null, updateWrapper);
    }

    @Override
    public List<ExpertReviewSingleItemDeduct> listAllHasScore(Integer expertId, Integer gradeId) {
        QueryWrapper<ExpertReviewSingleItemDeduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.ne("EVAL_RESULT", "");
        return expertReviewSingleItemDeductMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean checkDeductUnanimous(Integer[] gradeIds, String methodName1, String methodName2) {
        boolean sameFlag = true;
        List<Integer> countTemps = expertReviewSingleItemDeductMapper.listCountInconsistent(gradeIds, methodName1, methodName2);
        for (Integer countTemp : countTemps) {
            if (!ExecuteCode.SUCCESS.getCode().equals(countTemp)) {
                sameFlag = false;
                break;
            }
        }
        return sameFlag;
    }

    @Override
    public List<BidderResultDTO> listSumResult(Integer gradeId, Integer bidderId) {
        return expertReviewSingleItemDeductMapper.listSumResult(gradeId, bidderId);
    }

    @Override
    public ExpertReviewSingleItemDeduct getDeductById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        ExpertReviewSingleItemDeduct deduct = expertReviewSingleItemDeductMapper.selectById(id);
        GradeItem item = gradeItemService.getById(deduct.getGradeItemId());
        deduct.setGradeItem(item);
        return deduct;
    }

    @Override
    public List<ExpertReviewSingleItemDeduct> listDeductByBidderIdAndExpertId(Integer expertReviewId, Integer bidderId) {
        QueryWrapper<ExpertReviewSingleItemDeduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EXPERT_REVIEW_ID", expertReviewId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        return expertReviewSingleItemDeductMapper.selectList(queryWrapper);
    }

    @Override
    public List<BidderResultDTO> listResultIsConsistent(Integer gradeId, Integer bidderId, Integer expertSize) {
        ArrayList<BidderResultDTO> result = new ArrayList<>();

        List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductMapper.listResultCount(gradeId, bidderId);
        // 用于判断是否有一个结果数据是打分不一致，不一致的情况下则设置为当前item的id
        Integer gradeItemId = null;
        // 用于存储上一次结果的数量
        Integer scoreCont = null;
        // 用于存储上一次的打分结果
        Integer passResult = null;
        for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .gradeItemId(bidderResultDTO.getGradeItemId())
                    .build();
            if (!CommonUtil.isEmpty(gradeItemId) && bidderResultDTO.getGradeItemId().equals(gradeItemId)){
                if (scoreCont < bidderResultDTO.getScoreCont()){
                    newDto.setPassResult(bidderResultDTO.getPassResult());
                }else {
                    newDto.setPassResult(passResult);
                }
                result.add(newDto);
                gradeItemId = null;
                scoreCont = null;
                passResult = null;
                continue;
            }

            if (bidderResultDTO.getScoreCont().equals(expertSize)){
                newDto.setIsConsistent(true);
                newDto.setPassResult(bidderResultDTO.getPassResult());
                result.add(newDto);
            }else {
                gradeItemId = bidderResultDTO.getGradeItemId();
                scoreCont = bidderResultDTO.getScoreCont();
                passResult = bidderResultDTO.getPassResult();
            }
        }
        return result;
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<ExpertReviewSingleItemDeduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return expertReviewSingleItemDeductMapper.delete(queryWrapper);
    }
}
