package com.ejiaoyi.common.util;

import jodd.util.StringUtil;

/**
 * 金额转换工具类
 *
 * @author Make
 * @since 2020-07-15
 */
public class ConvertMoney {
    private static final String CHINESE_UNIT = "元=拾=佰=仟=万=拾=佰=仟=亿=拾=佰=仟=万";
    private static final String CHINESE_VALUE = "零壹贰叁肆伍陆柒捌玖";

    /**
     * 将小写金钱转换为大写
     */
    public static String moneyToChinese(String srcNumber) {

        if (StringUtil.isEmpty(srcNumber)) {
            return "";
        }
        srcNumber = srcNumber.trim();
        // 如果金额以.开始，则返回空字符串
        if (srcNumber.startsWith(".")) {
            return "";
        }

        StringBuffer chinese = new StringBuffer();
        StringBuilder decimalsChinese = new StringBuilder();
        String intNumber;
        StringBuilder decimals;
        // 判断所传过来的值，是整数还是小数
        if (!srcNumber.contains(".")) {
            srcNumber += ".00";
            intNumber = srcNumber.substring(0, srcNumber.indexOf("."));
            decimals = new StringBuilder("00");
        } else {
            intNumber = srcNumber.substring(0, srcNumber.indexOf("."));
            decimals = new StringBuilder(srcNumber.substring(srcNumber.indexOf(".") + 1, srcNumber.length()));
            while (decimals.length() < 2) {
                decimals.append("0");
            }
        }

        String [] chineseUnit1 = CHINESE_UNIT.split("=");
        // 转换整数部分
        for (int i = 0; i < intNumber.length(); i++) {
            chinese.append(CHINESE_VALUE.charAt(Integer.parseInt(srcNumber.charAt(i) + "")));
            chinese.append(chineseUnit1[intNumber.length() - 1 - i]);
        }

        boolean isZero = Integer.parseInt(decimals.substring(0, 2)) == 0;
        // 这个地方的判断主要是因为，零钱有小数部分，小数部分的默认值是0.0,默认的有小数部分。所以需要判断
        // 小数部分那个小数是否为0，如果为0，就需要转换小数部分了。
        // 计算小数部分
        if (!(decimals.length() == 2 && isZero)) {
            decimalsChinese.append(CHINESE_VALUE.charAt(Integer.parseInt(decimals.substring(0, 1))));
            decimalsChinese.append("角");
            decimalsChinese.append(CHINESE_VALUE.charAt(Integer.parseInt(decimals.substring(1, 2))));
            decimalsChinese.append("分");
        } else {
            decimalsChinese.append("整");
        }
        chinese.append(decimalsChinese);

        if (!"零元整".equals(chinese.toString())){
            while (chinese.indexOf("零零") != -1 || chinese.indexOf("零万") != -1
                    || chinese.indexOf("零亿") != -1 || chinese.indexOf("亿万") != -1
                    || chinese.indexOf("零佰") != -1 || chinese.indexOf("零元") != -1
                    || chinese.indexOf("零拾") != -1 || chinese.indexOf("零仟") != -1
                    || chinese.indexOf("零角") != -1 || chinese.indexOf("零分") != -1) {
                chinese = new StringBuffer(chinese.toString().replaceAll("零零", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零拾", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零万", "万"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零亿", "亿"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零元", "元"));
                chinese = new StringBuffer(chinese.toString().replaceAll("亿万", "亿零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零佰", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零仟", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零角", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零([拾佰仟])", "零"));
                chinese = new StringBuffer(chinese.toString().replaceAll("零分", ""));
            }

            while (chinese.indexOf("零") == 0 || chinese.indexOf("万") == 0 ||
                    chinese.indexOf("元") == 0 || chinese.indexOf("亿") == 0) {
                chinese = new StringBuffer(chinese.substring(1));
            }
        }

        return chinese.toString();
    }
}