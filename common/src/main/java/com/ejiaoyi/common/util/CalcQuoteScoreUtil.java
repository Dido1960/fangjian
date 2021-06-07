package com.ejiaoyi.common.util;

import com.ejiaoyi.common.dto.ArithmeticResult;
import com.ejiaoyi.common.enums.ArithmeticResultType;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 报价得分计算工具类
 *
 * @author Make
 * @since 2020/12/01
 */
public class CalcQuoteScoreUtil {
    /**
     * 计算报价得分，以报价平均值作为基准价(大于3家投标单位时，只去掉一个最高分和一个最低分)
     * 偏离不足1%的，按照插入法计算得分，结果四舍五入保留2位小数
     *
     * @param bidPrices 需要计算报价得分的报价集合
     * @param totalScore 总分数
     * @param upScore 评标价高于基准价一个百分点扣除的分值
     * @param downScore 评标价低于基准价一个百分点扣除的分值
     * @return 运算结果集
     */
    public static ArithmeticResult calcQuoteScoreOne(List<String> bidPrices, String totalScore, String upScore, String downScore) {
        ArithmeticResult arithmeticResult = ArithmeticResult.builder()
                .bidPrices(bidPrices)
                .totalScore(totalScore)
                .build();

        if (CollectionUtils.isEmpty(bidPrices)) {
            arithmeticResult.setStatus(ArithmeticResultType.ERROR);
            arithmeticResult.setErrorMsg("运算的数据为空集");
            return arithmeticResult;
        }

        if (!CommonUtil.isNum(totalScore)) {
            arithmeticResult.setStatus(ArithmeticResultType.ERROR);
            arithmeticResult.setErrorMsg("总分必须为数字，且不能为空");
            return arithmeticResult;
        }

        List<Double> offerPrices = new ArrayList<>();
        //存储总分的变量
        String totalPrice = "0";
        String basePrice;
        for (String bidPrice : bidPrices) {
            if (!CommonUtil.isNum(bidPrice)) {
                arithmeticResult.setStatus(ArithmeticResultType.ERROR);
                arithmeticResult.setErrorMsg("报价中包含非数字，错误报价为：" + bidPrice);
                return arithmeticResult;
            } else {
                offerPrices.add(Double.valueOf(bidPrice));
                totalPrice = CalcUtil.add(totalPrice, bidPrice);
            }
        }

        // 排序
        Collections.sort(offerPrices);
        double divide;
        if (bidPrices.size() <= 3) {
            // 投标人数小于5时，直接将算术平均值作为基准价
            divide = CalcUtil.divide(totalPrice, String.valueOf(offerPrices.size()));
        } else {
            // 总分去掉一个最高价和一个最低价
            List<String> remove = new ArrayList<>();
            remove.add(String.valueOf(offerPrices.get(offerPrices.size() - 1)));
            remove.add(String.valueOf(offerPrices.get(0)));
            totalPrice = CalcUtil.subtractMore(totalPrice, remove);
            // 在算出平均值*基准价系数作为评标基准价
            divide = CalcUtil.divide(totalPrice, String.valueOf(offerPrices.size() - 2));
        }
        basePrice = String.valueOf(divide);

        // 对应报价的单位得分
        List<String> bidPriceSroces = new ArrayList<>();
        // 对应报价的单位偏差率
        List<String> bidPriceOffsets = new ArrayList<>();
        for (String bidPrice : bidPrices) {
            // 偏差率 =（投标人评标价 - 评标基准价）/ 评标基准价
            double offsetD = CalcUtil.divide(CalcUtil.subtract(bidPrice, basePrice), basePrice, 4);
            // 将偏差率转换成百分比
            BigDecimal offset = BigDecimal.valueOf(CalcUtil.multiply(offsetD, 100d)).setScale(2, RoundingMode.HALF_UP);
            bidPriceOffsets.add(offset + "");
            // 如果偏差率为负数，即报价下浮
            BigDecimal bidPriceSroceB;
            if (offset.compareTo(new BigDecimal(0)) < 0) {
                // 最终得分=(-偏差率*单位下浮分数)+总分
                bidPriceSroceB = offset.multiply(new BigDecimal(downScore)).add(new BigDecimal(totalScore));
            } else {
                bidPriceSroceB = offset.multiply(new BigDecimal("-" + upScore)).add(new BigDecimal(totalScore));
            }

            BigDecimal bigDecimal = bidPriceSroceB.setScale(2, RoundingMode.HALF_UP);

            bidPriceSroces.add(bigDecimal.toString());
        }
        BigDecimal baseBig = new BigDecimal(basePrice);
        arithmeticResult.setStatus(ArithmeticResultType.SUCCESS);
        arithmeticResult.setErrorMsg(ArithmeticResultType.SUCCESS.getDesc());
        arithmeticResult.setBasePrice(String.valueOf(baseBig.setScale(2, RoundingMode.HALF_UP)));
        arithmeticResult.setBidPriceSroces(bidPriceSroces);
        arithmeticResult.setBidPriceOffsets(bidPriceOffsets);
        return arithmeticResult;
    }
}
