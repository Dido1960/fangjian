package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.Fdfs;

import java.util.List;

/**
 * <p>
 * 专家 服务类
 * </p>
 *
 * @author Make
 * @since 2020-09-30
 */
public interface IExpertService {
    /**
     * 评审环节完成情况
     * @param gradeIds 评标办法ids
     * @param evalProcess 流程环节
     * @return
     */
    boolean isGroupCompletion(String[] gradeIds, Integer evalProcess);

    /**
     * 获取开标记录表
     * @param bidSectionId 标段id
     * @return 开标记录表地址
     */
    List<Fdfs> listRecordTable(Integer bidSectionId);

    /**
     * 获取当前环节未完成的专家
     * @param expertUsers 当前标段的所有专家
     * @param gradeId 当前环节
     * @return
     */
    List<ExpertUser> listLinkNoFinishExpertUsers(List<ExpertUser> expertUsers,Integer gradeId);

}
