package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.dto.BidderRankingDTO;
import com.ejiaoyi.common.entity.CandidateResults;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 推荐候选人结果表 Mapper 接口
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-23
 */
@Component
public interface CandidateResultsMapper extends BaseMapper<CandidateResults> {

    /**
     * 推荐中标候选人，每个专家推荐意见
     * @param bidSectionId
     * @param bidderId 投标人主键
     * @return
     */
    List<CandidateResults> listBidderByPushWin(@Param("bidSectionId") Integer bidSectionId,
                                               @Param("bidderId") Integer bidderId) ;

    /**
     * 统计投标人相应名次的票数
     * @param bidSectionId 标段主键
     * @param ranking 名次
     * @return
     */
    List<BidderRankingDTO> listBidderRanking(@Param("bidSectionId") Integer bidSectionId,
                                               @Param("ranking") Integer ranking) ;

}
