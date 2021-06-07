package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 项目暂停记录表
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
@TableName("project_pause")
public class ProjectPause implements Serializable {

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
     * 标段ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 操作人员ID
     */
    @TableField("OPERATOR_ID")
    private Integer operatorId;

    /**
     * 操作人员名称
     */
    @TableField("OPERATOR_NAME")
    private String operatorName;

    /**
     * 暂停原因
     */
    @TableField("PAUSE_REASON")
    private String pauseReason;

    /**
     * 暂停状态(0:暂停 1:未暂停)
     */
    @TableField("PAUSE_STATUS")
    private Integer pauseStatus;
    /******************************自定义字段******************************/
}
