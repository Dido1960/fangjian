package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 复议日志
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
@TableName("reeval_log")
public class ReevalLog implements Serializable {

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
     * 操作人主键
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 操作人名称
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 复议原因
     */
    @TableField("REASON")
    private String reason;

    /**
     * 提交时间
     */
    @TableField("SUBMIT_TIME")
    private String submitTime;

    /**
     * 项目复议后，评标报告附件ID(对应fdfs主键ID)
     */
    @TableField("RE_EVAL_ANNEX_ID")
    private Integer reEvalAnnexId;

    /******************************自定义字段******************************/
}
