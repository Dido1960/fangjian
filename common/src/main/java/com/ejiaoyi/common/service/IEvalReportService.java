package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.FreeBackApply;
import com.ejiaoyi.common.enums.TemplateNameEnum;
import com.ejiaoyi.common.service.impl.ReportServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * 评标报告服务 接口
 * @author fengjunhong
 */
public interface IEvalReportService {

    /**
     * 返回模板对应的数据
     * @param bidSectionId 标段主键
     * @param templateNameEnum 报告模板
     * @return
     */
    Map<String,Object> getTemplateDataMap(Integer bidSectionId, TemplateNameEnum templateNameEnum);

    /**
     * 获取评标报告数据
     * @param bidSectionId 标段ID
     * @param templateNameEnum 模板名称
     * @param expertId 专家Id
     * @return
     */
    Map<String, Object> getEvalReportData(Integer bidSectionId, TemplateNameEnum templateNameEnum, Integer expertId);

    /**
     * 评审回退前数据
     * @param expertUser 评审专家
     * @param backTemplateNameEnum 回退模板
     * @return
     */
    Map<String,Object> getBackTempDataMap(ExpertUser expertUser, TemplateNameEnum backTemplateNameEnum, FreeBackApply freeBackApply);

    /**
     * 根据项目类型，返回子类对象
     * @param bidSectionId 标段主键
     * @return
     */
    ReportServiceImpl getReportDataMapSubclass(Integer bidSectionId);
}
