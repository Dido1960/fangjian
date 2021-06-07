package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.util.CommonUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 自由回退申请
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
@TableName("free_back_apply")
public class FreeBackApply implements Serializable {

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
     * 标段主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 申请回退的环节
     */
    @TableField("STEP")
    private Integer step;

    /**
     * 回退前评审环节
     */
    @TableField("STEP_NOW")
    private Integer stepNow;

    /**
     * 审核状态(0:未审核 1:审核通过 2:审核未通过)
     */
    @TableField("CHECK_STATUS")
    private String checkStatus;

    /**
     * 审核人主键ID
     */
    @TableField("CHECK_USER")
    private Integer checkUser;

    /**
     * 申请时间
     */
    @TableField("APPLY_TIME")
    private String applyTime;

    /**
     * 审核时间
     */
    @TableField("CHECK_TIME")
    private String checkTime;

    /**
     * 回退理由
     */
    @TableField("REASON")
    private String reason;

    /**
     * 申请人主键
     */
    @TableField("APPLY_USER")
    private Integer applyUser;

    /**
     * 回退审核通过后，各个环节专家的个人评审表附件ID(对应fdfs主键ID)
     */
    @TableField("FREE_BACK_ANNEX_ID")
    private Integer freeBackAnnexId;

    /******************************自定义字段******************************/

    /**
     * 处理人姓名
     */
    @TableField(exist = false)
    private String checkUserName;


    /**
     * 申请人姓名
     */
    @TableField(exist = false)
    private String applyUserName;


    /**
     * 申请回退环节
     */
    @TableField(exist = false)
    private String stepName;

    /**
     * 评审进展环节
     */
    @TableField(exist = false)
    private String beforeStepName;


    /**
     * 审核状态
     */
    @TableField(exist = false)
    private String checkStatusName;

    /**
     * 回退数据url
     */
    @TableField(exist = false)
    private String freeBackAnnexUrl;

    /******************************自定义字段******************************/

    public String getStepName() {
        if (!CommonUtil.isEmpty(this.step)){
            return EvalProcess.getCode(this.step).getRemake();
        }
        return "";
    }

    public String getBeforeStepName() {
        if (!CommonUtil.isEmpty(this.stepNow)){
            return EvalProcess.getCode(this.stepNow).getRemake();
        }
        return "";
    }

    public String getCheckStatusName() {
        switch (this.checkStatus){
            case "0":
                return "待审核";
            case "1":
                return "已通过";
            case "2":
                return "未通过";
            default:
                return "";
        }
    }
}
