package com.ejiaoyi.common.util;

import com.ejiaoyi.common.enums.TimeFormatter;
import jodd.util.StringUtil;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 日期时间工具类
 * 采用 JAVA 8 新特性 LocalDate LocalDateTime LocalTime Instant 进行实现
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class DateTimeUtil {

    /**
     * 网络时间矫正地址
     */
    private static final String INTERNET_TIME_ADDRESS = "http://www.baidu.com";

    /**
     * 获取网络时间
     *
     * @param timeFormatter 格式化规则
     * @return 时间字符串
     */
    public static String getInternetTime(TimeFormatter timeFormatter) {
        /*String internetTime = null;

        try {
            URL url = new URL(INTERNET_TIME_ADDRESS);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            long epochMilli = urlConnection.getDate();

            Instant instant = Instant.ofEpochMilli(epochMilli);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter.getCode(), Locale.CHINA);
            internetTime = formatter.format(localDateTime);
        } catch (IOException e) {
            internetTime = DateTimeUtil.getNowTime(timeFormatter);
        }
*/
        return DateTimeUtil.getNowTime(timeFormatter);
    }

    /**
     * 获取当前时间 本地时间
     *
     * @param timeFormatter 格式化规则
     * @return 时间字符串
     */
    private static String getNowTime(TimeFormatter timeFormatter) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter.getCode(), Locale.CHINA);
        return formatter.format(localDateTime);
    }

    /**
     * 返回时间差
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param timeFormatter 格式化方式
     * @return 时间差 seconds
     */
    public static long getTimeDiff(String startTime, String endTime, TimeFormatter timeFormatter) {
        return DateTimeUtil.getTimeDiff(startTime, endTime, TimeUnit.SECONDS, timeFormatter);
    }

    /**
     * 返回时间差
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param timeUnit      返回时间类型 default mill seconds
     * @param timeFormatter 格式化方式
     * @return 时间差
     */
    public static long getTimeDiff(String startTime, String endTime, TimeUnit timeUnit, TimeFormatter timeFormatter) {
        LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(timeFormatter.getCode()));
        LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(timeFormatter.getCode()));
        return DateTimeUtil.getTimeDiff(start, end, timeUnit);
    }

    /**
     * 返回时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差 seconds
     */
    public static long getTimeDiff(LocalDateTime startTime, LocalDateTime endTime) {
        return DateTimeUtil.getTimeDiff(startTime, endTime, TimeUnit.SECONDS);
    }

    /**
     * 返回时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param timeUnit  返回时间类型 default mill seconds
     * @return 时间差
     */
    public static long getTimeDiff(LocalDateTime startTime, LocalDateTime endTime, TimeUnit timeUnit) {
        //计算时间差
        Duration duration = Duration.between(startTime, endTime);

        if (timeUnit == null || timeUnit == TimeUnit.MILLISECONDS) {
            return duration.toMillis();
        }

        switch (timeUnit) {
            case DAYS:
                return duration.toDays();
            case HOURS:
                return duration.toHours();
            case MINUTES:
                return duration.toMinutes();
            case SECONDS:
                return duration.getSeconds();
        }

        return 0L;
    }

    /**
     * 将时间字符串转换为时间戳
     *
     * @param time          时间字符串
     * @param timeFormatter 格式化方式
     * @return 时间戳Long值
     */
    public static Long DateTimeToTimestamp(String time, TimeFormatter timeFormatter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter.getCode(), Locale.CHINA);
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 将毫秒转换为年月日时分秒
     *
     * @author GaoHuanjie
     */
    public static String getYearMonthDayHourMinuteSecond(long timeMillis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(timeMillis);
        int year=calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH) + 1;
        String mToMonth = null;
        if (String.valueOf(month).length() == 1) {
            mToMonth = "0" + month;
        } else {
            mToMonth = String.valueOf(month);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dToDay = null;
        if (String.valueOf(day).length() == 1) {
            dToDay = "0" + day;
        } else {
            dToDay=String.valueOf(day);
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hToHour = null;
        if (String.valueOf(hour).length() == 1) {
            hToHour = "0" + hour;
        } else {
            hToHour = String.valueOf(hour);
        }

        int minute=calendar.get(Calendar.MINUTE);
        String mToMinute = null;
        if (String.valueOf(minute).length() == 1) {
            mToMinute = "0" + minute;
        } else {
            mToMinute = String.valueOf(minute);
        }

        int second = calendar.get(Calendar.SECOND);
        String sToSecond = null;
        if (String.valueOf(second).length() == 1) {
            sToSecond = "0" + second;
        } else {
            sToSecond = String.valueOf(second);
        }
        return  year + "-" + mToMonth + "-" + dToDay + " " + hToHour + ":" + mToMinute + ":" + sToSecond;
    }

    /**
     * 比较2个日期
     * @param dt1 时间字符串
     * @param dt2 时间字符串
     * @param timeFormatter 格式化规则
     * @return
     */
    public static int compareDate(String dt1, String dt2, TimeFormatter timeFormatter) {
        Long long1 = DateTimeToTimestamp(dt1, timeFormatter);
        Long long2 = DateTimeToTimestamp(dt2, timeFormatter);

        return Long.compare(long1, long2);
    }

    /**
     * 对时间字符串进行时间拆分 格式： YYYY-mm-DD hh:MM:ss
     * @param timeStr 时间字符串
     * @return 封装键: "year": 年，"month": 月，"day": 日，"hover": 时，"minute": 分，"seconds": 时分秒，"minutesAndSeconds": 时分秒，"monthsAndDays"，年月日
     */
    public static Map<String, String> splitTimeStr(String timeStr) {
        Map<String, String> result = new HashMap<>();
        String monthsAndDays = StringUtil.split(timeStr, " ")[0];
        String minutesAndSeconds = StringUtil.split(timeStr, " ")[1];
        result.put("monthsAndDays",monthsAndDays);
        result.put("minutesAndSeconds",minutesAndSeconds);
        result.put("year",StringUtil.split(monthsAndDays, "-")[0]);
        result.put("month",StringUtil.split(monthsAndDays, "-")[1]);
        result.put("day",StringUtil.split(monthsAndDays, "-")[2]);
        result.put("hover",StringUtil.split(minutesAndSeconds, ":")[0]);
        result.put("minute",StringUtil.split(minutesAndSeconds, ":")[1]);
        result.put("seconds",StringUtil.split(minutesAndSeconds, ":")[2]);

        return result;
    }

    public static Map<String, String> splitTimeByYearStr(String timeStr) {
        Map<String, String> result = new HashMap<>();
        String monthsAndDays = StringUtil.split(timeStr, " ")[0];
        String minutesAndSeconds = StringUtil.split(timeStr, " ")[1];
        result.put("monthsAndDays","");
        result.put("minutesAndSeconds","");
        result.put("year",StringUtil.split(monthsAndDays, "-")[0]);
        result.put("month","");
        result.put("day","");
        result.put("hover","");
        result.put("minute","");
        result.put("seconds","");

        return result;
    }


    /**
     * 将毫秒值转换为时长字符串（format: XX小时XX分钟XX秒）
     * @param millis 毫秒值
     * @return 时长字符串
     */
    public static String parseTime(long millis) {
        StringBuilder stringBuffer = new StringBuilder();

        if (millis == 0){
            return "0秒 ";
        }

        // 计算小时
        stringBuffer.append((millis/3600000) == 0 ? "" : (millis/3600000)+"小时 ");
        // 计算分钟
        long currentMiles = millis%3600000;
        stringBuffer.append((currentMiles/60000) == 0 ? "" : (currentMiles/60000)+"分钟 ");
        // 计算秒
        currentMiles = currentMiles%60000;
        stringBuffer.append((currentMiles/1000) == 0 ? "" : (currentMiles/1000)+"秒 ");

        return stringBuffer.toString();
    }
}
