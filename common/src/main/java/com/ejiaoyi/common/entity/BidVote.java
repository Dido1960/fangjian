package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 专家推选评标组长投票信息
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
@TableName("bid_vote")
public class BidVote implements Serializable {

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
     * 标段信息记录主键ID
     */
    @TableField("BID_APPLY_ID")
    private Integer bidApplyId;

    /**
     * 专家主键ID
     */
    @TableField("BID_EXPERT_ID")
    private Integer bidExpertId;

    /**
     * 得票数
     */
    @TableField("COUNT")
    private Integer count;

    /**
     * 投票人(多个投标人用逗号隔开)
     */
    @TableField("VOTE_PERSON")
    private String votePerson;

    /**
     * 投票轮次
     */
    @TableField("VOTE_ROUND")
    private Integer voteRound;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/
}
