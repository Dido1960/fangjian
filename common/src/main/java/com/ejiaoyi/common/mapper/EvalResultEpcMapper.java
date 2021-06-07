package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.EvalResultEpc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 施工总承包项目简要评标结果 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface EvalResultEpcMapper extends BaseMapper<EvalResultEpc> {

    /**
     * 获取排名数据 按照排名升序排序
     * @param bidSectionId 标段ID
     * @return
     */
    List<EvalResultEpc> listRankingBidderByBsId(Integer bidSectionId);
}
