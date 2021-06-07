package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.ExpertReviewSingleItemDeductScore;
import com.ejiaoyi.common.entity.ExpertReviewSingleItemScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 打分制 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface ExpertReviewSingleItemScoreMapper extends BaseMapper<ExpertReviewSingleItemScore> {

    /**
     * 获取已经打了评分的列表
     *
     * @param expertId 专家ID
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @return
     */
    List<ExpertReviewSingleItemScore> listHasScore(@Param("expertId") Integer expertId, @Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);

    /**
     * 通过条件查询结果
     *
     * @param expertReviewSingleItemScore
     * @return
     */
    List<ExpertReviewSingleItemScore> listScoreResult(@Param("item") ExpertReviewSingleItemScore expertReviewSingleItemScore);

    /**
     * 通过条件获取所有的评审结果
     *
     * @param gradeIds   所有的gradeID
     * @param bidderId   投标人ID
     * @param expertId   专家id
     * @param gradeItemId   gradeItemId
     * @param evalProcess 评审流程
     * @param reviewType 评审类型
     * @param isScore    是否查询有数据的结果
     * @return 通过条件获取所有的评审结果
     */
    List<ExpertReviewSingleItemScore> listScoreBySth(@Param("gradeIds") String[] gradeIds, @Param("bidderId") Integer bidderId, @Param("expertId") Integer expertId, @Param("gradeItemId") Integer gradeItemId, @Param("evalProcess") Integer evalProcess, @Param("reviewType") Integer reviewType, @Param("isScore") boolean isScore);

    /**
     * 获取当前结果数据是否一致，返回的列表如果有数据大于投标人数量则表示，有不一致的数据！
     *
     * @param gradeIds   所有的gradeId
     * @param reviewType 评审类型
     * @return 获取当前结果数据是否一致
     */
    List<Integer> listScoreConsistent(@Param("gradeIds") String[] gradeIds, @Param("reviewType") Integer reviewType);

    /**
     * 获取平均打分
     *
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @param size     专家人数
     * @return 获取平均打分
     */
    String getAvgScore(@Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId, @Param("size") Integer size);

    /**
     * 获取当前投标人当前reviewType打分结果类型数量，通过统计不同打分的item总数，如果返回数量大于item总数则表示打分不一致
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @return 获取当前投标人当前reviewType打分结果类型数量
     */
    Integer countScoreResult(@Param("gradeIds")String[] gradeIds, @Param("reviewType") Integer reviewType, @Param("bidderId") Integer bidderId);

    /**
     * 获取当前投标人当前reviewType 的平均分
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @param expertNum 专家人数
     * @return 获取当前投标人当前reviewType 的平均分
     */
    String getAvgScoreForReview(@Param("gradeIds")String[] gradeIds, @Param("evalProcess") Integer evalProcess, @Param("reviewType") Integer reviewType, @Param("bidderId") Integer bidderId, @Param("expertNum") Integer expertNum);


    /**
     * 通过条件获取平均分，当专家人数大于5时采用去除一个最低分去除一个最高分进行计算。
     * 当 bidderId 为null时 返回数据为当前标段所有的投标人打分数据！
     * @param gradeIds 需要查询的gradeIDs
     * @param gradeId gradeId
     * @param bidSectionId 标段ID
     * @param bidderId 投标人id
     * @param gradeItemId itemID
     * @return 结果数据
     */
    List<BidderResultDTO> getAvgScoreBySth(@Param("gradeIds")String[] gradeIds, @Param("gradeId") Integer gradeId, @Param("bidSectionId") Integer bidSectionId, @Param("bidderId") Integer bidderId, @Param("gradeItemId") Integer gradeItemId);
}
