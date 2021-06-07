package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.LikeCount;
import com.ejiaoyi.common.mapper.LikeCountMapper;
import com.ejiaoyi.common.service.ILikeCountService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点赞统计 服务实现类
 * </p>
 *
 * @author samzqr
 * @since 2020-12-12
 */
@Service
public class LikeCountServiceImpl extends ServiceImpl<LikeCountMapper, LikeCount> implements ILikeCountService {

    @Autowired
    LikeCountMapper likeCountMapper;

    @Override
    public boolean insert(LikeCount likeCount) {
        if (CommonUtil.isEmpty(likeCount.getId())) {
            return likeCountMapper.insert(likeCount) > 0;
        } else {
            return likeCountMapper.updateById(likeCount) > 0;
        }
    }

    @Override
    public LikeCount LikeByBidSectionId(Integer bidSectionId) {
        QueryWrapper<LikeCount> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        return likeCountMapper.selectOne(wrapper);
    }

}
