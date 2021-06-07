package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.FreeBackApply;
import com.ejiaoyi.common.enums.TemplateNameEnum;

import java.util.List;
import java.util.Map;

/**
 * 评标报告获取数据接口
 * @author fengjunhong
 * @since 2020-11-25
 */
public interface IReportService {

    /**
     * 【评标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId);

    /**
     * 【流标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId);

    /**
     * 【回退报告】模板
     * @param freeBackApply 回退申请
     * @return
     */
    List<TemplateNameEnum> listBackTempNameEnum(FreeBackApply freeBackApply);

    /**
     * 封面数据
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getCoverReportData(Integer bidSectionId);

    /**
     * 资格审查
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getQualificationReviewDataMap(Integer bidSectionId);

    /**
     * 初步评审
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getFirstStepDataMap(Integer bidSectionId);

    /**
     * 详细评审
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getDetailReviewDataMap(Integer bidSectionId);


    /**
     * 详细评审专家个人打分表
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getDetailPersonReviewDataMap(Integer bidSectionId);


    /**
     * 报价得分表
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getQuoteScoreDataMap(Integer bidSectionId);

    /**
     * 评标得分汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getEvaluationScoreDataMap(Integer bidSectionId);

    /**
     * 评审意见汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getReviewOpinionDataMap(Integer bidSectionId);

    /**
     * 评审工作履职情况记录表（不包含业主）
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getExpertUsersNoOwner(Integer bidSectionId);

    /**
     * 商务评审表
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getShangWuReviewDataMap(Integer bidSectionId);

    /**
     * 技术标
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getJiShuGroupReviewDataMap(Integer bidSectionId);

    /**
     * 返回当前类型的所有回退报告模板
     * 【说明】：返回的是最终合成pdf的顺序
     * @param bidSectionId 标段主键
     * @return
     */
    List<TemplateNameEnum> listResumptionTemplateNameEnum(Integer bidSectionId);

    /**
     * 否决投标人名单
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getNoPassBiddersDataMap(Integer bidSectionId);

    /**
     * 评标简易结果
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String,Object> getBidderEvalResultDataMap(Integer bidSectionId);

}
