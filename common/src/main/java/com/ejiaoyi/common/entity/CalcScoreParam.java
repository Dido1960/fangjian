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
 * 计算报价得分参数
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("calc_score_param")
public class CalcScoreParam implements Serializable {

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
     * 标段id
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 评标价得分计算方式
     */
    @TableField("EVAL_PRICE_SCORE_METHOD")
    private String evalPriceScoreMethod;

    /**
     * 评标价得分计算方式详细描述
     */
    @TableField("EVAL_PRICE_SCORE_METHOD_DESC")
    private String evalPriceScoreMethodDesc;

    /**
     * 总分
     */
    @TableField("TOTAL_SCORE")
    private String totalScore;

    /**
     * 评标基准价
     */
    @TableField("BASE_PRICE")
    private String basePrice;

    /**
     * 修改后的评标基准价
     */
    @TableField("UPDATE_BASE_PRICE")
    private String updateBasePrice;

    /**
     * 偏差率保留小数位数
     */
    @TableField("OFFSET_RATE_PRECISION")
    private Integer offsetRatePrecision;

    /**
     * 评标价每高于评标基准价一个百分点的扣分值
     */
    @TableField("GT_BASE_PRICE_DEDUCTION")
    private String gtBasePriceDeduction;

    /**
     * 评标价每低于评标基准价一个百分点的扣分值
     */
    @TableField("LT_BASE_PRICE_DEDUCTION")
    private String ltBasePriceDeduction;


}
