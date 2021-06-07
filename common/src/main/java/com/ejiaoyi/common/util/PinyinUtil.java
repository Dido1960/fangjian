package com.ejiaoyi.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具
 *
 * @author Z0001
 * @since 2020-05-11
 */
public class PinyinUtil {

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String cn2FirstSpell(String chinese) {
        chinese = PinyinUtil.filterStr(chinese);

        StringBuilder buffer = new StringBuilder();
        char[] arr = chinese.toCharArray();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : arr) {
            if (c > 128) {
                try {
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (pinyinArr != null) {
                        buffer.append(pinyinArr[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String cn2Spell(String chinese) {
        chinese = PinyinUtil.filterStr(chinese);

        StringBuilder buffer = new StringBuilder();
        char[] arr = chinese.toCharArray();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : arr) {
            if (c > 128) {
                try {
                    buffer.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * 过滤字符串
     *
     * @param source 字符串
     * @return 过滤后的字符串
     */
    private static String filterStr(String source) {
        // 过滤字符串，只允许字母、数字和中文
        return source.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
    }
}