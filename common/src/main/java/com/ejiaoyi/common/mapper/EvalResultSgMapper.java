package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.EvalResultSg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 施工项目简要评标结果 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface EvalResultSgMapper extends BaseMapper<EvalResultSg> {

    /**
     * 通过标段ID获取 投标人排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultSg> listRankingBidderByBsId(@Param("bidSectionId") Integer bidSectionId);

}
