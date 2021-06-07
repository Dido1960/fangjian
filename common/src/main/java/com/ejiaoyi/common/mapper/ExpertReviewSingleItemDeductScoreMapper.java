package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.ExpertReviewSingleItemDeduct;
import com.ejiaoyi.common.entity.ExpertReviewSingleItemDeductScore;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 扣分打分制 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface ExpertReviewSingleItemDeductScoreMapper extends BaseMapper<ExpertReviewSingleItemDeductScore> {

    /**
     * 通过条件查询结果
     *
     * @param expertReviewSingleItemDeductScore
     * @return
     */
    List<ExpertReviewSingleItemDeductScore> listDeductScore(@Param("item") ExpertReviewSingleItemDeductScore expertReviewSingleItemDeductScore);

    /**
     * 对所有的投标人进行一键不扣分
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param expertId   专家id
     * @return 对所有的投标人进行一键不扣分
     */
    void updateOneKeyNoDeduct(@Param("gradeIds") String[] gradeIds, @Param("reviewType") Integer reviewType, @Param("expertId") Integer expertId);

    /**
     * 通过条件获取所有的评审结果
     *
     * @param gradeIds    所有的gradeID
     * @param bidderId    投标人ID
     * @param expertId    专家id
     * @param gradeItemId gradeItemId
     * @param reviewType  评审类型
     * @param isScore     是否查询有数据的结果
     * @return 获取所有的评审结果
     */
    List<ExpertReviewSingleItemDeductScore> listDeductScoreBySth(@Param("gradeIds") String[] gradeIds, @Param("bidderId") Integer bidderId, @Param("expertId") Integer expertId, @Param("gradeItemId") Integer gradeItemId, @Param("reviewType") Integer reviewType, @Param("isScore") boolean isScore);

    /**
     * 获取当前结果数据是否一致，返回的列表如果有数据大于投标人数量则表示，有不一致的数据！
     *
     * @param gradeIds   所有的gradeId
     * @param reviewType 评审类型
     * @return 获取当前结果数据是否一致
     */
    List<Integer> listDeductScoreConsistent(@Param("gradeIds") String[] gradeIds, @Param("reviewType") Integer reviewType);

    /**
     * 获取平均扣分
     *
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @param expertNum     专家人数
     * @return 获取平均扣分
     */
    String getAvgDeductScore(@Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId, @Param("expertNum") Integer expertNum);

    /**
     * 获取当前投标人当前reviewType打分结果类型数量，通过统计不同打分的item总数，如果返回数量大于item总数则表示打分不一致
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @return 获取当前投标人当前reviewType打分结果类型数量
     */
    Integer countDeductScoreResult(@Param("gradeIds")String[] gradeIds, @Param("reviewType") Integer reviewType, @Param("bidderId") Integer bidderId);

    /**
     * 获取当前投标人当前reviewType 的平均分
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @param expertNum 专家人数
     * @return 获取当前投标人当前reviewType 的平均分
     */
    String getAvgDeductScoreForReview(@Param("gradeIds")String[] gradeIds, @Param("reviewType") Integer reviewType, @Param("bidderId") Integer bidderId, @Param("expertNum") Integer expertNum);
}
