package com.ejiaoyi.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 专家对投标人某评审因素打分情况表
 * </p>
 *
 * @author Make
 * @since 2020-10-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expert_review_bidder_score")
public class ExpertReviewBidderScore implements Serializable {

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
     * 评审专家ID
     */
    @TableField("EXPERT_ID")
    private Integer expertId;

    /**
     * 投标人ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 评分标准ID
     */
    @TableField("GRADE_ID")
    private Integer gradeId;

    /**
     * 该评审因素所得分数
     */
    @TableField("SCORE")
    private String score;

    /**
     * 所得分数所占该评审因素总分的权重占比
     */
    @TableField("RATE")
    private String rate;

    /**
     * 占比低于60%的原因
     */
    @TableField("REASON")
    private String reason;

    /******************************自定义字段******************************/

    /**
     * 评分标准名称
     */
    @TableField(exist = false)
    private String gradeName;

    /**
     * 评分标准总分
     */
    @TableField(exist = false)
    private String gradeTotalScore;

    /**
     * 投标人名称
     */
    @TableField(exist = false)
    private String bidderName;
}
