package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.LikeCount;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 点赞统计 Mapper 接口
 * </p>
 *
 * @author samzqr
 * @since 2020-12-12
 */
@Component
public interface LikeCountMapper extends BaseMapper<LikeCount> {

}
