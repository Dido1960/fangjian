package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.enums.ArithmeticResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 运算结果集
 *
 * @author Make
 * @since 2020/9/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArithmeticResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运算结果状态
     */
    ArithmeticResultType status;

    /**
     * 总分
     */
    String totalScore;

    /**
     * 错误信息
     */
    String errorMsg;

    /**
     * 运算的方法
     */
    String arithmeticName;

    /**
     * 报价信息
     */
    List<String> bidPrices;

    /**
     * 当前的规则计算出的基准价
     */
    String basePrice;

    /**
     * 对应报价的单位得分
     */
    List<String> bidPriceSroces;

    /**
     * 对应报价的单位偏差率
     */
    List<String> bidPriceOffsets;

    /**
     * 投标人集合
     */
    List<Bidder> bidders;
}
