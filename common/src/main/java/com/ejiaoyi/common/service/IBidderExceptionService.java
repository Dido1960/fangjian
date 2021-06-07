package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidderException;
import com.ejiaoyi.common.entity.BidderOpenInfo;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/23 17:55
 */
public interface IBidderExceptionService {
    Integer addBidderException(BidderException bidderException);

    Integer updateBidderException(BidderException bidderException);

    /**
     * 通过bio的标段id，投标人id，异常类型获取异常理由，查不到返回null;
     * @param bidderOpenInfo
     * @param exceType
     * @return
     */
    BidderException getExceptionReason(BidderOpenInfo bidderOpenInfo, int exceType);

    /**
     * 条件查询唯一的异常信息，只获取一条
     * 查询条件 1.有id直接查id
     * 2.通过标段id，用户id，异常类型，以及是否启用，获取最后一条数据
     * 查不到返回原有条件数据
     * @param bidderException
     * @return
     */
    BidderException getExceptionReasonByReason(BidderException bidderException);

    /**
     * 新增或更新理由
     * @param userId 当前用户主键
     * @param userName 当前用户名称
     * @param bidderException
     * @return
     */
    Integer saveOrUpdateReason(Integer userId, String userName, BidderException bidderException);
}
