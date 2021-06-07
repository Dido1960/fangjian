package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BackPushStatus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 回退申请推送状态 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IBackPushStatusService extends IService<BackPushStatus> {

    /**
     * 对推送信息初始化，添加所有专家的推送信息
     * @param backPushStatus 推送数据
     * @return
     */
    Integer initBackPush(BackPushStatus backPushStatus);

    /**
     * 通过专家ID获取所有的推送信息
     * @param expertId 专家ID
     * @return
     */
    List<BackPushStatus> listBackPushByExpertId(Integer expertId);

    /**
     * 更具Id更新推送消息
     * @param backPushStatus 推送数据
     * @return
     */
    Integer updateBackPush(BackPushStatus backPushStatus);

    /**
     * 根据标段ID以级专家ID将所有通知更新为已通知
     * @param bidSectionId 标段ID
     * @param expertId 专家ID
     * @return
     */
    Integer updateAllBackPush(Integer bidSectionId, Integer expertId);
}
