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
 * 回退申请推送状态
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("back_push_status")
public class BackPushStatus implements Serializable {

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
     * 回退申请ID
     */
    @TableField("BACK_ID")
    private Integer backId;

    /**
     * 专家ID
     */
    @TableField("EXPERT_ID")
    private Integer expertId;

    /**
     * 推送结果(0:未接收 1:已接收)
     */
    @TableField("PUSH_RESULT")
    private Integer pushResult;


}
