package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.FreeBackApply;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-10-7 16:30
 */
public interface IEvalBackService {
    /**
     * 判断当前标段是否满足回退条件
     * @return
     */
    JsonData validBack();
    /**
     * 保存回退申请
     * @param freeBackApply 回退数据
     * @return
     */
    JsonData addFreeBackApply(FreeBackApply freeBackApply);

    /**
     * 获取当前标段评标进行的环节
     * @param bidSectionId 标段ID
     * @return
     */
    Integer getEvalFlowNow(Integer bidSectionId);

    /**
     * 获取回退申请同志消息
     * @return
     */
    JsonData getBackPush();

    /**
     * 更新已经阅读的消息
     * @return
     */
    Integer updateBackPush();

    /**
     * 获取是否小组评审结束
     * @param gradeIds 评标办法
     * @param evalProcess 评标环节
     * @return 是否结束
     */
    boolean isGroupCompletion(String[] gradeIds, Integer evalProcess);

}
