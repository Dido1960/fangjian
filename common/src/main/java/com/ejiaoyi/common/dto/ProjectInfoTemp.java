package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.CalcScoreParam;
import lombok.Data;

/**
 * 项目信息临时表（只用于前后台数据交互）
 *
 * @author Make
 */
@Data
public class ProjectInfoTemp {
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 招标项目名称
     */
    private String tenderProjectName;

    /**
     * 招标项目编号
     */
    private String tenderProjectCode;

    /**
     * 项目主键id
     */
    private Integer projectId;

    /**
     * 招标项目主键id
     */
    private Integer tenderProjectId;

    /**
     * 招标文件主键id
     */
    private Integer tenderDocId;

    /**
     * 标段主键id
     */
    private Integer bidSectionId;

    /**
     * 标段名称
     */
    private String bidSectionName;

    /**
     * 标段编号
     */
    private String bidSectionCode;

    /**
     * 投标文件递交截止时间
     */
    private String bidDocReferEndTime;

    /**
     * 开标时间
     */
    private String bidOpenTime;

    /**
     * 招标人
     */
    private String tendererName;

    /**
     * 招标代理机构
     */
    private String tenderAgencyName;

    /**
     * 招标代理机构联系方式
     */
    private String tenderAgencyPhone;

    /**
     * 评标专家人数
     */
    private Integer expertCount;

    /**
     * 招标代表人数
     */
    private Integer representativeCount;

    /**
     * 是否网上开标
     */
    private Integer bidOpenOnline;

    /**
     * 是否远程异地评标
     */
    private Integer remoteEvaluation;

    /**
     * 澄清文件上传id
     */
    private Integer clarifyFileId;

    /**
     * 关联评标办法,多个评标办法间用逗号隔开
     */
    private String gradeId;

    /**
     * 招标工程量清单文件标识码
     */
    private String xmlUid;

    /**
     * 分类代码
     */
    private String bidClassifyCode;

    /**
     * 招标文件附件ID
     */
    private Integer docFileId;

    /**
     * 招标文件附件文件名
     */
    private String docFileName;

    /**
     * 区划名称
     */
    private String regName;

    /**
     * 区划主键
     */
    private Integer regId;

    /**
     * 投标保证金
     */
    private String marginAmount;

    /**
     * 招标组织形式
     */
    private String tenderOrganizeForm;

    /**
     * 评标办法
     */
    private String evaluationMethod;

    /**
     * 招标方式
     */
    private String tenderMode;

    /**
     * 是否纸质标
     */
    private String paperEval;

    /**
     * 计算报价得分参数
     */
    private CalcScoreParam calcScoreParam;
}
