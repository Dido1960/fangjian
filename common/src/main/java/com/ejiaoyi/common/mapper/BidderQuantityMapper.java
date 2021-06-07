package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.BidderQuantity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 投标人工程量清单服务信息 Mapper 接口
*
* @author Kevin
* @since 2020-12-11
*/
@Component
public interface BidderQuantityMapper extends BaseMapper<BidderQuantity> {

    /**
     * 获取所有未完成的投标人工程量清单服务信息
     * @return
     */
    List<BidderQuantity> listNotCompleteBidderQuantity();

    /**
     * 获取需要执行算术性分析服务的清单信息
     * @return 执行算术性分析服务的清单信息
     */
    List<BidderQuantity> listBidderQuantityNeedDoArithmeticAnalysis();

    /**
     * 获取需要执行零负报价分析服务的清单信息
     * @return 执行零负报价分析服务的清单信息
     */
    List<BidderQuantity> listBidderQuantityNeedDoPriceAnalysis();

    /**
     * 获取需要执行取费基础分析服务的清单信息
     * @return 执行取费基础分析服务的清单信息
     */
    List<BidderQuantity> listBidderQuantityNeedDoRuleAnalysis();

    /**
     * 获取需要执行错漏项分析服务的清单信息
     * @return 执行错漏项分析服务的清单信息
     */
    List<BidderQuantity> listBidderQuantityNeedDoStructureAnalysis();

    /**
     * 获取所有注册服务未执行的投标人工程量清单服务信息
     * @return
     */
    List<BidderQuantity> listNotDoServiceBidderQuantity();

    /**
     * 获取需要注册的分析服务
     * @return 需要注册的分析服务的清单信息
     */
    List<BidderQuantity> listNeedRegisterAnalysisService();
}
