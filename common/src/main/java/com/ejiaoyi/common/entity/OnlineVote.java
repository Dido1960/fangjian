package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 在线投票
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
@TableName("online_vote")
public class OnlineVote implements Serializable {

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
     * 标段编号
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 主题
     */
    @TableField("THEME")
    private String theme;

    /**
     * 选项1
     */
    @TableField("OPTION1")
    private String option1;

    /**
     * 选项2
     */
    @TableField("OPTION2")
    private String option2;

    /**
     * 选项1得票数
     */
    @TableField("VOTES1")
    private Integer votes1;

    /**
     * 选项2得票数
     */
    @TableField("VOTES2")
    private Integer votes2;

    /**
     * 参与投票的人数
     */
    @TableField("TOTAL")
    private Integer total;

    /**
     * 状态（0，结束；1投票中）
     */
    @TableField("STATUS")
    private Integer status;

    /******************************自定义字段******************************/
}
