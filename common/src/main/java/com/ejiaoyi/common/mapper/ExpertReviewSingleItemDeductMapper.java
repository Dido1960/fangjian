package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.ExpertReviewSingleItemDeduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 扣分制 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface ExpertReviewSingleItemDeductMapper extends BaseMapper<ExpertReviewSingleItemDeduct> {

    /**
     * 条件获取专家对企业评审单项评审结果集合
     *
     * @param expertReviewSingleItemDeduct 条件
     * @return 专家对企业评审单项评审结果
     */
    List<ExpertReviewSingleItemDeduct> listExpertReviewSingleItemDeduct(@Param("item") ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct);

    /**
     * 获取已经打了评分的列表
     *
     * @param expertId 专家ID
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @return 获取已经打了评分的列表
     */
    List<ExpertReviewSingleItemDeduct> listHasScore(@Param("expertId") Integer expertId, @Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);

    /**
     * 对当前方法名判断 统计打分一致情况
     * @param gradeIds 所有的gradeId
     * @param methodName1 方法名称1
     * @param methodName2 方法名称2
     * @return 统计打分一致情况
     */
    List<Integer> listCountInconsistent(@Param("gradeIds") Integer[] gradeIds, @Param("methodName1") String methodName1, @Param("methodName2") String methodName2);

    /**
     * 获取结果统计 按照投标人 item 分组 统计每项 扣分与不扣分总和
     * @param gradeId gradeId
     * @return 按照投标人 item 分组 统计每项 扣分与不扣分总和
     */
    List<BidderResultDTO>  listSumResult(@Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);

    /**
     * 获取对当前投标人当前方法的评审结果项，统计打分形式个数，如果总数等于专家人数说明打分结果一致，否则会出现两种相同的item,并可以进行统计实际的打分情况！
     * @param gradeId gradeID
     * @param bidderId 投标人iD
     * @return 分装数据
     */
    List<BidderResultDTO> listResultCount(@Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);
}
