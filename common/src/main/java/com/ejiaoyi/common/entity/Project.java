package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 项目信息
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 项目编号
     */
    @TableField("PROJECT_CODE")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("PROJECT_NAME")
    private String projectName;

    /**
     * 项目所在行政区域代码
     */
    @TableField("REGION_CODE")
    private String regionCode;

    /**
     * 投资项目统一代码
     */
    @TableField("INVEST_PROJECT_CODE")
    private String investProjectCode;

    /**
     * 项目地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 项目法人
     */
    @TableField("LEGAL_PERSON")
    private String legalPerson;

    /**
     * 项目行业分类
     */
    @TableField("INDUSTRIES_TYPE")
    private String industriesType;

    /**
     * 资金来源
     */
    @TableField("SOURCE_FUND")
    private String sourceFund;

    /**
     * 出资比例
     */
    @TableField("CONTRIBUTION_SCALE")
    private String contributionScale;

    /**
     * 项目规模
     */
    @TableField("PROJECT_SCALE")
    private String projectScale;

    /**
     * 联系人
     */
    @TableField("CONTACT_PERSON")
    private String contactPerson;

    /**
     * 联系方式
     */
    @TableField("CONTACT_INFORMATION")
    private String contactInformation;

    /**
     * 项目审批文件名称
     */
    @TableField("APPROVAL_NAME")
    private String approvalName;

    /**
     * 项目审批文号
     */
    @TableField("APPROVAL_NUMBER")
    private String approvalNumber;

    /**
     * 项目审批单位
     */
    @TableField("APPROVAL_AUTHORITY")
    private String approvalAuthority;

    /**
     * 项目建立时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 行政区划主键ID
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /******************************自定义字段******************************/
}
