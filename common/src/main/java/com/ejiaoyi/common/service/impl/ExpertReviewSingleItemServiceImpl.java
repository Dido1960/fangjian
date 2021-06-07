package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewSingleItemMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 专家对企业评审单项评审结果 合格制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ExpertReviewSingleItemServiceImpl extends ServiceImpl<ExpertReviewSingleItemMapper, ExpertReviewSingleItem> implements IExpertReviewSingleItemService {

    @Autowired
    private ExpertReviewSingleItemMapper expertReviewSingleItemMapper;
    @Autowired
    private IExpertReviewService expertReviewService;
    @Autowired
    private IGradeItemService gradeItemService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IExpertUserService expertUserService;

    @Override
    @RedissonLock(key = "#expertId + '_' + #currentGrade.id")
    public ExpertReview initExpertSingleItem(Integer bidSectionId, Integer expertId, Grade currentGrade, List<Bidder> bidders) {
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

        ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem.builder()
                .expertId(expertId)
                .expertReviewId(expertReview.getId())
                .gradeId(currentGrade.getId())
                .gradeType(Integer.valueOf(currentGrade.getGradeType()))
                .build();

        for (Bidder bidder : bidders) {
            for (GradeItem gradeItem : currentGrade.getGradeItems()) {
                expertReviewSingleItem.setId(null);
                expertReviewSingleItem.setBidderId(bidder.getId());
                expertReviewSingleItem.setGradeItemId(gradeItem.getId());
                expertReviewSingleItemMapper.insert(expertReviewSingleItem);
            }

        }
        return expertReviewService.getExpertReview(query);
    }


    @Override
    public List<ExpertReviewSingleItem> listExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem) {
        return expertReviewSingleItemMapper.listExpertReviewSingleItem(expertReviewSingleItem);
    }

    @Override
    public List<ExpertReviewSingleItem> listExpertReviewSingleItem(String[] gradeIds) {
        return expertReviewSingleItemMapper.listExpertReviewSingleItemForComment(gradeIds);
    }

    @Override
    public Integer updateSingleItem(ExpertReviewSingleItem expertReviewSingleItem) {
        Assert.notNull(expertReviewSingleItem, "param expertReviewSingleItem can not be empty");
        Assert.notNull(expertReviewSingleItem.getId(), "param id can not be null");
        expertReviewSingleItemMapper.updateById(expertReviewSingleItem);
        return expertReviewSingleItem.getId();
    }

    @Override
    public List<ExpertReviewSingleItem> listHasScore(Integer expertId, Integer gradeId, Integer bidderId) {
        return expertReviewSingleItemMapper.listHasScore(expertId, gradeId, bidderId);
    }

    @Override
    public Integer updateListItem(Integer gradeId, Integer userId) {
        UpdateWrapper<ExpertReviewSingleItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("GRADE_ID", gradeId);
        updateWrapper.eq("EXPERT_ID", userId);
        updateWrapper.set("EVAL_RESULT", 1);
        return expertReviewSingleItemMapper.update(null, updateWrapper);
    }

    @Override
    public ExpertReviewSingleItem getExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem) {
        QueryWrapper<ExpertReviewSingleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != expertReviewSingleItem.getId(), "ID", expertReviewSingleItem.getId());
        queryWrapper.eq(null != expertReviewSingleItem.getExpertId(), "EXPERT_ID", expertReviewSingleItem.getExpertId());
        queryWrapper.eq(null != expertReviewSingleItem.getGradeType(), "GRADE_TYPE", expertReviewSingleItem.getGradeType());
        queryWrapper.eq(null != expertReviewSingleItem.getBidderId(), "BIDDER_ID", expertReviewSingleItem.getBidderId());
        queryWrapper.eq(null != expertReviewSingleItem.getGradeId(), "GRADE_ID", expertReviewSingleItem.getGradeId());
        queryWrapper.eq(null != expertReviewSingleItem.getGradeItemId(), "GRADE_ITEM_ID", expertReviewSingleItem.getGradeItemId());

        return expertReviewSingleItemMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BidderResultDTO> listGradeConsistent(Integer gradeId) {
        return expertReviewSingleItemMapper.listGradeConsistent(gradeId);
    }

    @Override
    public List<ExpertReviewSingleItem> listItemByBidderIdAndExpertId(Integer expertReviewId, Integer bidderId) {
        QueryWrapper<ExpertReviewSingleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EXPERT_REVIEW_ID", expertReviewId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        return expertReviewSingleItemMapper.selectList(queryWrapper);
    }

    @Override
    public List<ExpertReviewSingleItem> listAllHasScore(Integer expertId, Integer gradeId) {
        QueryWrapper<ExpertReviewSingleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.ne("EVAL_RESULT", "");
        return expertReviewSingleItemMapper.selectList(queryWrapper);
    }

    @Override
    public Integer update(ExpertReviewSingleItem expertReviewSingleItem) {
        Assert.notNull(expertReviewSingleItem, "param expertReviewSingleItem can not be empty");
        Assert.notNull(expertReviewSingleItem.getId(), "param id can not be null");
        expertReviewSingleItemMapper.updateById(expertReviewSingleItem);
        return expertReviewSingleItem.getId();
    }

    @Override
    public boolean listCountByEvalResult(Integer[] gradeIds) {
        boolean sameFlag = true;
        List<Integer> countTemps = expertReviewSingleItemMapper.listCountByEvalResult(gradeIds);
        for (Integer countTemp : countTemps) {
            if (!ExecuteCode.SUCCESS.getCode().equals(countTemp)) {
                sameFlag = false;
                break;
            }
        }
        return sameFlag;
    }

    @Override
    public List<BidderResultDTO> listQualifiedInfo(Integer gradeId, Integer bidSectionId) {
        return expertReviewSingleItemMapper.listQualifiedInfo(gradeId, bidSectionId);
    }

    @Override
    public ExpertReviewSingleItem getSingleById(Integer singId) {
        Assert.notNull(singId, "param Id can not be empty");
        ExpertReviewSingleItem expertReviewSingleItem = expertReviewSingleItemMapper.selectById(singId);
        expertReviewSingleItem.setGradeItemContent(gradeItemService.getById(expertReviewSingleItem.getGradeItemId()).getItemContent());
        return expertReviewSingleItem;
    }

    @Override
    public List<BidderResultDTO>  listGradeResult(Integer gradeId, Integer bidderId) {
        return expertReviewSingleItemMapper.listGradeResult(gradeId, bidderId);
    }

    @Override
    public List<BidderResultDTO> listItemResult(Integer gradeId, Integer bidderId) {
        List<BidderResultDTO>  result = new ArrayList<>();
        List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemMapper.listGradeResult(gradeId, bidderId);
        Integer itemId = null;
        for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .gradeItemId(bidderResultDTO.getGradeItemId())
                    .isConsistent(true)
                    .build();
            if (!CommonUtil.isEmpty(itemId) && itemId.equals(bidderResultDTO.getGradeItemId())){
                newDto.setIsConsistent(false);
                result.add(newDto);
                itemId = null;
                continue;
            }
            itemId = bidderResultDTO.getGradeItemId();
            newDto.setPassResult(bidderResultDTO.getPassResult());
            result.add(newDto);
        }

        return result;
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<ExpertReviewSingleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return expertReviewSingleItemMapper.delete(queryWrapper);
    }

    @Override
    public List<ExpertReviewSingleItem> sortExpertReviewSingleByStep(Integer bidSectionId, List<ExpertReviewSingleItem> expertReviewSingleItems) {
        if (!CommonUtil.isEmpty(expertReviewSingleItems)){
            // 1、获取当前标段类型
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            String bidProjectCode = bidSection.getBidClassifyCode();
            // 项目类型枚举
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidProjectCode);
            switch (bidProtype){
                case EPC:
                  return sortEpcExpertReviewSingle(expertReviewSingleItems);
                default:
                  return sortOtherExpertReviewSingle(expertReviewSingleItems);
            }
        }
        return expertReviewSingleItems;
    }

    @Override
    public List<ExpertReviewSingleItem> scrapBiddersResult(Integer bidSectionId, List<ExpertReviewSingleItem> expertReviewSingleItems) {
        if (!CommonUtil.isEmpty(expertReviewSingleItems)){
//            List<ExpertReviewSingleItem> isCharManItem = new ArrayList<>();
//            // 当前标段组长
//            ExpertUser expertUser = expertUserService.getChairmanByBidSectionId(bidSectionId);
//            for (ExpertReviewSingleItem item : expertReviewSingleItems) {
//                if (expertUser.getId().equals(item.getExpertId())){
//                    isCharManItem.add(item);
//                }
//            }
            // 获取当前标段类型
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            String bidProjectCode = bidSection.getBidClassifyCode();
            // 项目类型枚举
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidProjectCode);
            return scarpExpertReviewSingle(bidProtype,expertReviewSingleItems);
//            switch (bidProtype){
//                case EPC:
//                    return scarpEpcExpertReviewSingle(expertReviewSingleItems);
//                case QUALIFICATION:
//                    return scarpZgysExpertReviewSingle(expertReviewSingleItems);
//                default:
//                    return scarpOtherExpertReviewSingle(expertReviewSingleItems);
//            }
        }
        return expertReviewSingleItems;
    }

    /**
     * 排序EPC评审意见
     * @param singleItems 评审意见
     * @return
     */
    private  List<ExpertReviewSingleItem> sortEpcExpertReviewSingle(List<ExpertReviewSingleItem> singleItems){
        // 重新排序的评审数据
        List<ExpertReviewSingleItem> newSingleItems = new ArrayList<>();
        Assert.notEmpty("param singleItems can't be empty");
        if (!CommonUtil.isEmpty(singleItems)){
            for (ExpertReviewSingleItem item : singleItems) {
                Grade grade = gradeService.getGradeById(item.getGradeId());
                // 获取评审类型
                EvalProcess code = EvalProcess.getCode(grade.getReviewProcess());
                switch (Objects.requireNonNull(code)){
                    case QUALIFICATION:
                    case PRELIMINARY:
                    case DETAILED:
                        newSingleItems.add(item);
                        break;
                    default:
                        break;
                }
            }
            return newSingleItems;
        }
        return singleItems;
    }

    /**
     * 排序除Epc之外评审意见
     * @param singleItems 评审意见
     * @return
     */
    private  List<ExpertReviewSingleItem> sortOtherExpertReviewSingle(List<ExpertReviewSingleItem> singleItems){
        // 重新排序的评审数据
        List<ExpertReviewSingleItem> newSingleItems = new ArrayList<>();
        Assert.notEmpty("param singleItems can't be empty");
        if (!CommonUtil.isEmpty(singleItems)){
            for (ExpertReviewSingleItem item : singleItems) {
                Grade grade = gradeService.getGradeById(item.getGradeId());
                // 获取评审类型
                EvalProcess code = EvalProcess.getCode(grade.getReviewProcess());
                switch (Objects.requireNonNull(code)){
                    case PRELIMINARY:
                    case DETAILED:
                        newSingleItems.add(item);
                        break;
                    default:
                        break;
                }
            }
            return newSingleItems;
        }
        return singleItems;
    }

    /**
     * 废标结果
     * @param singleItems 评审意见
     * @return
     */
    private  List<ExpertReviewSingleItem> scarpExpertReviewSingle(BidProtype bidProtype, List<ExpertReviewSingleItem> singleItems){
        // 评审意见
        List<ExpertReviewSingleItem> newSingleItems = new ArrayList<>();
        Assert.notEmpty("param singleItems can't be empty");
        if (!CommonUtil.isEmpty(singleItems)){
            for (ExpertReviewSingleItem item : singleItems) {
                Grade grade = gradeService.getGradeById(item.getGradeId());
                // 获取评审类型
                EvalProcess code = EvalProcess.getCode(grade.getReviewProcess());
                // 项目类型
                if (BidProtype.QUALIFICATION.equals(bidProtype)){
                    switch (Objects.requireNonNull(code)){
                        case PRELIMINARY:
                        case DETAILED:
                            newSingleItems.add(item);
                            break;
                        default:
                            break;
                    }
                } else if (BidProtype.EPC.equals(bidProtype)){
                    switch (Objects.requireNonNull(code)){
                        case QUALIFICATION:
                        case PRELIMINARY:
                            newSingleItems.add(item);
                            break;
                        default:
                            break;
                    }
                } else {
                    // 其他类型，只能再初步评审废标
                    if (EvalProcess.PRELIMINARY.equals(code)){
                        newSingleItems.add(item);
                    }
                }
            }
            return newSingleItems;
        }
        return singleItems;
    }

}
