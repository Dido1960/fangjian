package com.ejiaoyi.common.util;

import com.ejiaoyi.common.enums.TimeFormatter;

import java.util.Random;

/**
 * 支付相关工具类
 *
 * @author fengjunhong
 * @since 2020/5/27
 */
public class RandomStrUtil {

    /**
     * 获取订单号
     *
     * @return
     */
    public static synchronized String getOrderNo() {
        //当前时间戳+随机数
        String dateTime = DateTimeUtil.getInternetTime(TimeFormatter.PAY_YYYY_HH_DD_HH_MM_SS);
        Random randomIndex = new Random();
        int random1 = randomIndex.nextInt(10);
        int random2 = randomIndex.nextInt(10);
        int random3 = randomIndex.nextInt(10);
        int random4 = randomIndex.nextInt(10);
        return dateTime + random1 + random2 + random3 + random4;
    }

    /**
     * @param count 订单号位数(>14,不足位数由随机数补充) 小于8只返回时间戳
     * @Description 生成订单号：
     * 当前时间戳+随机任意位数
     * @Author liuguoqiang
     * @Date 2020-8-5 15:05
     */
    public static synchronized String getOrderNoByCunt(Integer count) {
        //当前时间戳+随机数
        String dateTime = DateTimeUtil.getInternetTime(TimeFormatter.PAY_YYYY_HH_DD_HH_MM_SS);
        StringBuilder result = new StringBuilder(dateTime);
        Random randomIndex = new Random();
        if (count != null && count > 8) {
            for (int i = 0; i < count - 14; i++) {
                result.append(randomIndex.nextInt(10));
            }
        }
        return result.toString();
    }

    /**
     * @Description 生成随机位整数串
     * @Author liuguoqiang
     * @Date 2020-8-25 16:34
     */
    public static synchronized String getRandomStr(Integer count) {
        StringBuilder result = new StringBuilder();
        Random randomIndex = new Random();
        if (count != null && count > 0) {
            for (int i = 0; i < count; i++) {
                result.append(randomIndex.nextInt(10));
            }
        }
        return result.toString();
    }
}
