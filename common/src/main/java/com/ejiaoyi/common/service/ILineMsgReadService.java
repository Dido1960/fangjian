package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.LineMsgRead;

/**
 * <p>
 * 网上开标消息阅读情况 服务类
 * </p>
 *
 * @author Make
 * @since 2020-08-07
 */
public interface ILineMsgReadService {

   /**
    * 获取未读的网上开标消息条数
    *
    * @param lineMsgRead  阅读情况
    * @param bidSectionId 标段id
    * @return
    */
   Integer getUnReadCount(LineMsgRead lineMsgRead,Integer bidSectionId);

   /**
    * 根据条件更新网上消息阅读情况
    * @param bidSectionId 标段编号id
    * @param userId 用户id
    * @param roleType 用户类型
    */
   void updateUserLineMsgRead(Integer bidSectionId, Integer userId, Integer roleType);
}
