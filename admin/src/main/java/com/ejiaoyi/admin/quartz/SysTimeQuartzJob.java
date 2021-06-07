package com.ejiaoyi.admin.quartz;

import com.ejiaoyi.common.annotation.QuartzLog;
import com.ejiaoyi.common.enums.TimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * quartz系统时间 信息
 *
 * @author Z0001
 * @since 2020-5-20
 */
@Slf4j
public class SysTimeQuartzJob extends QuartzJobBean {


    /**
     * 网络时间矫正地址
     *  中国科学院国家授时中心
     */
    private static final String INTERNET_TIME_ADDRESS = "http://www.ntsc.ac.cn/";


    @Override
    @QuartzLog
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            //TODO 以下为定时任务 逻辑
            URL url = new URL(INTERNET_TIME_ADDRESS);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            long epochMilli = urlConnection.getDate();

            Instant instant = Instant.ofEpochMilli(epochMilli);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA);
            String internetTime  = formatter.format(localDateTime);
            updateSysDateTime(internetTime.split(" ")[0],internetTime.split(" ")[1]);
            log.info("更新系统时间成功");
        } catch (Exception e) {
          e.printStackTrace();
          log.warn("更新系统时间失败");
        }
    }

    /**
     * 修改系统时间
     * yyyy-MM-dd HH:mm:ss
     *
     * @param dataStr_ 2017-11-11   yyyy-MM-dd
     * @param timeStr_ 11:11:11     HH:mm:ss
     */
    public static void updateSysDateTime(String dataStr_, String timeStr_) {
        try {
            String osName = System.getProperty("os.name");
            // Window 系统
            if (osName.matches("^(?i)Windows.*$")) {
                String cmd;
                // 格式：yyyy-MM-dd
                cmd = " cmd /c date " + dataStr_;
                Runtime.getRuntime().exec(cmd);
                // 格式 HH:mm:ss
                cmd = " cmd /c time " + timeStr_;
                Runtime.getRuntime().exec(cmd);
                System.out.println("windows 时间修改");
            } else if (osName.matches("^(?i)Linux.*$")) {
                // Linux 系统 格式：yyyy-MM-dd HH:mm:ss   date -s "2017-11-11 11:11:11"
                FileWriter excutefw = new FileWriter("/usr/updateSysTime.sh");
                BufferedWriter excutebw = new BufferedWriter(excutefw);
                excutebw.write("date -s \"" + dataStr_ + " " + timeStr_ + "\"\r\n");
                excutebw.close();
                excutefw.close();
                String cmd_date = "sh /usr/updateSysTime.sh";
                Runtime.getRuntime().exec(cmd_date);
                System.out.println("cmd :" + cmd_date + " date :" + dataStr_ + " time :" + timeStr_);
                System.out.println("linux 时间修改");
            } else {
                System.out.println("操作系统无法识别");
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }


}
