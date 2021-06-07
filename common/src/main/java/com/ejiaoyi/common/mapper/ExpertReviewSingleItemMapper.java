package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.ExpertReviewSingleItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 合格制 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface ExpertReviewSingleItemMapper extends BaseMapper<ExpertReviewSingleItem> {
    /**
     * 条件获取专家对企业评审单项评审结果集合
     * @param expertReviewSingleItem 条件
     * @return 专家对企业评审单项评审结果
     */
    List<ExpertReviewSingleItem> listExpertReviewSingleItem(@Param("item") ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 获取已经打了评分的列表
     *
     * @param expertId 专家ID
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @return
     */
    List<ExpertReviewSingleItem> listHasScore(@Param("expertId") Integer expertId, @Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);

    /**
     * 当前grandID以经打分的结果汇总 用于判断当前结果是否一致
     * @param gradeId id
     * @return
     */
    List<BidderResultDTO> listGradeConsistent(@Param("gradeId")Integer gradeId);

    /**
     * 获取评分标准 按照评分项所有评标专家评分的distinct的个数  如果不等于一 说明评分不一致
     *
     * @param gradeIds 评分标准ids
     * @return
     */
    List<Integer> listCountByEvalResult(@Param("gradeIds") Integer[] gradeIds);

    /**
     * 获取投标人评审项的结果（合格制）
     *
     * @param gradeId 评审标准id
     * @param bidSectionId 标段id
     * @return
     */
    List<BidderResultDTO> listQualifiedInfo(@Param("gradeId") Integer gradeId, @Param("bidSectionId") Integer bidSectionId);

    /**
     * 获取当前打分结果不同种类。打分结果统计
     * @param gradeId gradeId
     * @param bidderId bidderId
     * @return 打分结果统计。
     */
    List<BidderResultDTO> listGradeResult(@Param("gradeId")Integer gradeId, @Param("bidderId") Integer bidderId);

    /**
     * 获取指定评审点结果集合 评审意见
     * @param gradeIds 评审项id集合
     * @return
     */
    List<ExpertReviewSingleItem> listExpertReviewSingleItemForComment(@Param("gradeIds") String[] gradeIds);
}
