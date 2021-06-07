package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.OnlineInfo;

/**
 * <p>
 * 在线用户信息表 服务类
 * </p>
 *
 * @author lesgod
 * @since 2021-01-16
 */
public interface IOnlineInfoService extends IService<OnlineInfo> {

    void saveOrUpdateOnline(OnlineInfo onlineInfo);

    void removeBySessionId(String id);

    String pagedOnlineInfo();
}
