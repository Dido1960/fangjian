package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.ClarifyAnswer;

import java.util.List;

/**
 * <p>
 * 澄清答疑文件 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-11
 */
public interface IClarifyAnswerService extends IService<ClarifyAnswer> {

    /**
     * 根据标段id获取所有的澄清答疑文件
     * @param bidSectionId 标段id
     * @return 该标段的所有澄清答疑文件
     */
    List<ClarifyAnswer> listClarifyAnswerBySectionId(Integer bidSectionId);
}
