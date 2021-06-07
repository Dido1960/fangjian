package com.ejiaoyi.common.util;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 精准计算工具类
 *
 * @author Z0001
 * @since 2020/3/30
 */
public class CalcUtil {

    /**
     * 默认运算精度
     */
    private static final int DEFAULT_SCALE = 2;

    /**
     * 加法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数和
     */
    public static double add(double v1, double v2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(v2));
        return decimal1.add(decimal2).doubleValue();
    }

    /**
     * 加法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数和
     */
    public static String add(String v1, String v2) {
        Assert.notEmpty(v1, "param v1 can not be empty !");
        Assert.notEmpty(v2, "param v2 can not be empty !");

        BigDecimal decimal1 = new BigDecimal(v1);
        BigDecimal decimal2 = new BigDecimal(v2);
        return decimal1.add(decimal2).toString();
    }

    /**
     * 减法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(v2));
        return decimal1.subtract(decimal2).doubleValue();
    }

    /**
     * 减法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数差
     */
    public static String subtract(String v1, String v2) {
        Assert.notEmpty(v1, "param v1 can not be empty !");
        Assert.notEmpty(v2, "param v2 can not be empty !");

        BigDecimal decimal1 = new BigDecimal(v1);
        BigDecimal decimal2 = new BigDecimal(v2);
        return decimal1.subtract(decimal2).toString();
    }

    /**
     * 乘法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(v2));
        return decimal1.multiply(decimal2).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数积
     */
    public static String multiply(String v1, String v2) {
        Assert.notEmpty(v1, "param v1 can not be empty !");
        Assert.notEmpty(v2, "param v2 can not be empty !");

        BigDecimal decimal1 = new BigDecimal(v1);
        BigDecimal decimal2 = new BigDecimal(v2);
        return decimal1.multiply(decimal2).toString();
    }

    /**
     * 除法运算 计算精度 2 舍入模式 RoundingMode.HALF_UP
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数商
     */
    public static double divide(double v1, double v2) {
        return CalcUtil.divide(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算 舍入模式 RoundingMode.HALF_UP
     *
     * @param v1    v1
     * @param v2    v2
     * @param scale 计算精度
     * @return 参数商
     */
    public static double divide(double v1, double v2, int scale) {
        return CalcUtil.divide(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算
     *
     * @param v1           v1
     * @param v2           v2
     * @param scale        计算精度
     * @param roundingMode 舍入模式
     * @return 参数商
     */
    public static double divide(double v1, double v2, int scale, RoundingMode roundingMode) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(v2));
        return decimal1.divide(decimal2, scale, roundingMode).doubleValue();
    }

    /**
     * 除法运算 计算精度 2 舍入模式 RoundingMode.HALF_UP
     *
     * @param v1 v1
     * @param v2 v2
     * @return 参数商
     */
    public static double divide(String v1, String v2) {
        return CalcUtil.divide(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算 舍入模式 RoundingMode.HALF_UP
     *
     * @param v1    v1
     * @param v2    v2
     * @param scale 计算精度
     * @return 参数商
     */
    public static double divide(String v1, String v2, int scale) {
        return CalcUtil.divide(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算
     *
     * @param v1           v1
     * @param v2           v2
     * @param scale        计算精度
     * @param roundingMode 舍入模式
     * @return 参数商
     */
    public static double divide(String v1, String v2, int scale, RoundingMode roundingMode) {
        BigDecimal decimal1 = new BigDecimal(v1);
        BigDecimal decimal2 = new BigDecimal(v2);
        return decimal1.divide(decimal2, scale, roundingMode).doubleValue();
    }

    /**
     * 进行精确舍入 计算精度 2 舍入模式 RoundingMode.HALF_UP
     *
     * @param v v
     * @return 参数舍入值
     */
    public static double round(double v) {
        return CalcUtil.round(v, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 进行精确舍入 舍入模式 RoundingMode.HALF_UP
     *
     * @param v     v
     * @param scale 计算精度
     * @return 参数舍入值
     */
    public static double round(double v, int scale) {
        return CalcUtil.round(v, scale, RoundingMode.HALF_UP);
    }

    /**
     * 进行精确舍入
     *
     * @param v            v
     * @param scale        计算精度
     * @param roundingMode 舍入模式
     * @return 参数舍入值
     */
    public static double round(double v, int scale, RoundingMode roundingMode) {
        Assert.checkBetween(scale, 0, 100);

        BigDecimal decimal = new BigDecimal(Double.toString(v));
        return decimal.setScale(scale, roundingMode).doubleValue();
    }

    /**
     * 数字累加
     * @param numberList 需要累加的集合
     * @return
     */
    public static String addMore(List<String> numberList){
        String sum = "0";

        for (String num : numberList) {
            sum = add(sum, num);
        }

        return sum;
    }

    /**
     * 数字累减
     * @param subtractNum (被减数)
     * @param numberList 减数集合
     * @return
     */
    public static String subtractMore(String subtractNum, List<String> numberList){
        String last = subtractNum;

        for (String num : numberList) {
            last = subtract(last, num);
        }

        return last;
    }
}
