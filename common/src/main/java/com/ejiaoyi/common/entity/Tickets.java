package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 在线投票数量
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
@TableName("tickets")
public class Tickets implements Serializable {

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
     * 投票ID
     */
    @TableField("ONLINE_VOTE_ID")
    private Integer onlineVoteId;

    /**
     * 投票专家ID
     */
    @TableField("EXPERT_ID")
    private Integer expertId;

    /**
     * 投票选项（1对应option1；2对应option2）
     */
    @TableField("OPTIONS")
    private Integer options;

    /******************************自定义字段******************************/
}
