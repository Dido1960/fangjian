package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.OnlineInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 在线用户信息表 Mapper 接口
 * </p>
 *
 * @author lesgod
 * @since 2021-01-16
 */
@Component
public interface OnlineInfoMapper extends BaseMapper<OnlineInfo> {

    Integer selectOnlineBySessionId(@Param("sessionId") String sessionId);

    void removeBySessionId(@Param("sessionId") String sessionId);
}
