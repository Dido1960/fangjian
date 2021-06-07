package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运维跟踪记录
 * </p>
 *
 * @author Mike
 * @since 2021-03-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("operation_maintenance_track_record")
public class OperationMaintenanceTrackRecord implements Serializable {

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
     * 运维人员ID
     */
    @TableField("OPERATION_MAINTENANCE_ID")
    private Integer operationMaintenanceId;

    /**
     * 运维人员名称
     */
    @TableField("OPERATION_MAINTENANCE_NAME")
    private String operationMaintenanceName;

    /**
     * 是否异常(1:正常   0：异常)
     */
    @TableField("ABNORMAL")
    private String abnormal;

    /**
     * 异常处理时长（分钟）
     */
    @TableField("ABNORMAL_TIME")
    private Integer abnormalTime;

    /**
     * 跟踪记录
     */
    @TableField("REMARK")
    private String remark;


}
