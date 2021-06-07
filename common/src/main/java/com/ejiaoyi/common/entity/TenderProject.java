package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 招标项目信息
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
@TableName("tender_project")
public class TenderProject implements Serializable {

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
     * 项目主键ID
     */
    @TableField("PROJECT_ID")
    private Integer projectId;

    /**
     * 招标项目编号
     */
    @TableField("TENDER_PROJECT_CODE")
    private String tenderProjectCode;

    /**
     * 招标项目名称
     */
    @TableField("TENDER_PROJECT_NAME")
    private String tenderProjectName;

    /**
     * 招标项目类型
     */
    @TableField("TENDER_PROJECT_TYPE")
    private String tenderProjectType;

    /**
     * 招标项目所在行政区域代码
     */
    @TableField("REGION_CODE")
    private String regionCode;

    /**
     * 行政区划主键ID
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 招标内容与范围及招标方案说明
     */
    @TableField("TENDER_CONTENT")
    private String tenderContent;

    /**
     * 项目业主名称
     */
    @TableField("OWNER_NAME")
    private String ownerName;

    /**
     * 招标人名称
     */
    @TableField("TENDERER_NAME")
    private String tendererName;

    /**
     * 招标人类别
     */
    @TableField("TENDERER_CODE_TYPE")
    private String tendererCodeType;

    /**
     * 招标人代码
     */
    @TableField("TENDERER_CODE")
    private String tendererCode;

    /**
     * 招标人角色
     */
    @TableField("TENDERER_ROLE")
    private String tendererRole;

    /**
     * 招标代理机构名称
     */
    @TableField("TENDER_AGENCY_NAME")
    private String tenderAgencyName;

    /**
     * 招标代理机构电话
     */
    @TableField("TENDER_AGENCY_PHONE")
    private String tenderAgencyPhone;

    /**
     * 招标代理机构类别
     */
    @TableField("TENDER_AGENCY_CODE_TYPE")
    private String tenderAgencyCodeType;

    /**
     * 招标代理机构角色
     */
    @TableField("TENDER_AGENCY_ROLE")
    private String tenderAgencyRole;

    /**
     * 招标方式
     */
    @TableField("TENDER_MODE")
    private String tenderMode;

    /**
     * 招标组织形式
     */
    @TableField("TENDER_ORGANIZE_FORM")
    private String tenderOrganizeForm;

    /**
     * 招标项目建立时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 监督部门名称
     */
    @TableField("SUPERVISE_DEPT_NAME")
    private String superviseDeptName;

    /**
     * 监督部门代码
     */
    @TableField("SUPERVISE_DEPT_CODE")
    private String superviseDeptCode;

    /**
     * 审核部门名称
     */
    @TableField("APPROVE_DEPT_NAME")
    private String approveDeptName;

    /**
     * 审核部门代码
     */
    @TableField("APPROVE_DEPT_CODE")
    private String approveDeptCode;

    /**
     * 招标项目创建人(单点用户ID)
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 招标代理机构统一社会信用代码
     */
    @TableField("TENDER_AGENCY_CODE")
    private String tenderAgencyCode;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /******************************自定义字段******************************/
}
