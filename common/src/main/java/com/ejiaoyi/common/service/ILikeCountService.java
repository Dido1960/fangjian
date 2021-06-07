package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.LikeCount;

/**
 * <p>
 * 点赞统计 服务类
 * </p>
 *
 * @author samzqr
 * @since 2020-12-12
 */
public interface ILikeCountService extends IService<LikeCount> {
    /**
     * 记录点赞数
     * @param likeCount 点赞数
     * @return
     */
    boolean insert(LikeCount likeCount);

    /**
     * 根据标段id查询点赞
     * @param bidSectionId 标段id
     * @return
     */
    LikeCount LikeByBidSectionId(Integer bidSectionId);
}
