package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.TenderDoc;

/**
 * 清标V3.0 接口
 * @author fengjunhong
 * @version 1.0
 * @date 2021-4-21 14:30
 */
public interface IClearBidV3Service {

    /**
     * 创建清标服务
     * @param section 标段信息
     * @param tenderDoc 招标文件信息
     */
    String createClearServer(BidSection section, TenderDoc tenderDoc) throws Exception;

    /**
     * 否决投标人
     *
     * @param section   标段信息
     */
    void cancelBidders(BidSection section);

    /**
     * 获得令牌
     *
     * @return {@link String}
     */
    String getToken();

    /**
     * 业务code
     *
     * 规则：前缀 + 标段id + 当前时间戳 + 6位随机数
     * 举例：FJ_JQ_95_20210422200821477324
     *
     * @param prefix       前缀
     * @param bidSectionId 标段ID
     * @return {@link String}
     */
    String getYwCode(String prefix,Integer bidSectionId);

    /**
     * 得到显示清标展示的模块
     *
     * @param tenderDoc 招标文件信息
     * @return {@link String}
     */
    String getShowClearModule(TenderDoc tenderDoc);

    /**
     * 设定清标的模块
     *
     * @param ywCode      yw代码
     * @param clearModule 清标的模块
     */
    void setClearModule(String ywCode,String clearModule);

    /**
     * 报价得分是否计算完成
     *
     * @return {@link Integer}
     */
    Integer priceScoreFlag(String ywCode);
}
