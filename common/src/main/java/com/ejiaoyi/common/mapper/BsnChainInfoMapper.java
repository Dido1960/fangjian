package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.BsnChainInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Desc:
 * @author: yyb
 * @date: 2020-8-18 13:45
 */
@Component
public interface BsnChainInfoMapper extends BaseMapper<BsnChainInfo> {

    /**
     * 获取最新的一条区块信息的地址
     * @param bidderId 投标人id
     * @param type 类型
     * @return
     */
    BsnChainInfo getLastBsnChainInfo(@Param("bidderId") Integer bidderId, @Param("type") Integer type);

}
