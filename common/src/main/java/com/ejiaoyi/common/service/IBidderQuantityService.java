package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.BidderQuantity;

import java.util.List;

/**
 * 投标人工程量清单服务信息 服务类接口
 *
 * @author Make
 * @since 2020-12-15
 */
public interface IBidderQuantityService extends IService<BidderQuantity> {

    /**
     * 初始化投标人工程量清单服务信息，如果存在就更新，如果不存在则插入
     * @param bidderQuantity 初始化的数据
     * @return
     */
    BidderQuantity initBidderQuantity(BidderQuantity bidderQuantity);

    /**
     * 通过投标工程量清单序列号获取投标人工程量清单服务信息
     * @param bidXmlUid 投标清单uuid
     * @return
     */
    BidderQuantity getBidderQuantityByBidXmlUid(String bidXmlUid);

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

}
