package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.BidSectionRelate;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/15 09:52
 */
public interface IBidSectionRelateService extends IService<BidSectionRelate> {
    /**
     * 通过BidSectionId获取
     * @param bSId
     * @return
     */
    BidSectionRelate getBidSectionRelateByBSId(Integer bSId);

    /**
     * 添加
     * @param bidSectionRelate
     * @return
     */
    Integer addBidSectionRelate(BidSectionRelate bidSectionRelate);

    /**
     * 修改
     * @param bidSectionRelate
     * @return
     */
    Integer updateBidSectionRelate(BidSectionRelate bidSectionRelate);

    /**
     * 通过标段ID获取relate
     * @param bidSectionRelate 标段关联信息
     * @return 更新结果
     */
    Boolean updateRelateBySectionId(BidSectionRelate bidSectionRelate);

    /**
     * 清除评标报告id
     * @param id relateID
     */
    void updateClearReportId(Integer id);
}
