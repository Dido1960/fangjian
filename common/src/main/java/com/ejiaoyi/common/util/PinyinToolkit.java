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
 * @since 2020-03-17
 */
public class PinyinToolkit {

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String cn2FirstSpell(String chinese) {
        chinese = filterStr(chinese);
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (_t != null) {
                        pybf.append(_t[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String cn2Spell(String chinese) {
        chinese = filterStr(chinese);
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }

    /**
     * 替换非空字符串
     *
     * @param source
     * @return
     */
    private static String filterStr(String source) {
        //过滤字符串，只允许字母、数字和中文
        return source.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
    }
}