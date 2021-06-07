package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.BidderReviewResultScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 企业评审单项的评审结果表 打分制 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface BidderReviewResultScoreMapper extends BaseMapper<BidderReviewResultScore> {

    /**
     * 获取投标人总得分
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId   投标人ID
     * @return 获取投标人总得分
     */
    String getTotalScore(@Param("gradeIds") String[] gradeIds, @Param("reviewType") Integer reviewType, @Param("bidderId") Integer bidderId);
}
