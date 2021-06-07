package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 中标候选人表
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("candidate_success")
public class CandidateSuccess implements Serializable {

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
     * 投标人ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 推荐名次
     */
    @TableField("RANKING")
    private Integer ranking;

    /**
     * 推荐理由
     */
    @TableField("REASON")
    private String reason;

    /**
     * 投标人1：小组投票产生 2：组长推选产生
     */
    @TableField("BIDDER_FROM")
    private Integer bidderFrom;

    /*************************************自定义字段****************************************/
    @TableField(exist = false)
    private String bidderName;

    /**
     * 专家姓名
     */
    @TableField(exist = false)
    private String expertName;

    /**
     * 投标报价类型
     */
    @TableField(exist = false)
    private String bidderPriceType;

    /**
     * 报价
     */
    @TableField(exist = false)
    private String bidderPrice;


}
