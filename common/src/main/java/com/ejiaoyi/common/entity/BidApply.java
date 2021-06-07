package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 评标申请记录
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
@TableName("bid_apply")
public class BidApply implements Serializable {

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
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 专家组长主键ID
     */
    @TableField("CHAIR_MAN")
    private Integer chairMan;

    /**
     * 投票轮次
     */
    @TableField("VOTE_COUNT")
    private Integer voteCount;

    /******************************自定义字段******************************/
}
