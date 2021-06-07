package com.ejiaoyi.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 通用工具类
 *
 * @author Z0001
 * @since 2020-5-12
 */
public class CommonUtil {

    /**
     * 判断对象是否为空 长度是否为0
     *
     * @param obj 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof List) {
                return CollectionUtils.isEmpty((List) obj);
            }

            if (obj instanceof Array) {
                return CollectionUtils.isEmpty(Arrays.asList(obj));
            }

            if (obj instanceof String) {
                return StringUtils.isEmpty(String.valueOf(obj));
            }

            if (obj instanceof Long) {
                return (Long) obj == 0;
            }
        }

        return false;
    }

    /**
     * 判断是否为数字
     *
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isNum(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 判断对象是否为空，如果对象为ArrayList，且不为空，则还要判断是否有长度
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isNull(Object obj) {
        List list = null;
        String str = null;
        boolean flag = false;
        String[] strs = null;
        HashMap map=null;
        Long l;
        if (obj == null) {
            flag = true;
        } else {
            if ("ArrayList".equals(obj.getClass().getSimpleName())) {
                list = (ArrayList) obj;
                if (list.size() == 0) {
                    flag = true;
                }
            }
            if ("String[]".equals(obj.getClass().getSimpleName())) {
                strs = (String[]) obj;
                if (strs.length == 1) {
                    if (CommonUtil.isNull(strs[0])) {
                        flag = true;
                    }
                }
            }
            if ("String".equals(obj.getClass().getSimpleName())) {
                str = (String) obj;
                if ("".equals(str) || "null".equals(str) || "NULL".equals(str)) {
                    flag = true;
                }
            }
            if ("Long".equals(obj.getClass().getSimpleName())) {
                l = (Long) obj;
                if (l == 0) {
                    flag = true;
                }
            }
            if ("HashMap".equals(obj.getClass().getSimpleName())) {
                map = (HashMap) obj;
                if (map.isEmpty()) {
                    flag = true;
                }
            }
        }
        return flag;
    }

}
