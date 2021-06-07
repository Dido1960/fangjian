package com.ejiaoyi.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具类
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Slf4j
public class HttpUtil {

    /**
     * 定义未知字符串常量
     */
    private static final String UNKNOWN = "unknown";

    /**
     * 获取请求的真实IP地址
     *
     * @param request 请求
     * @return 真实IP地址
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("HTTP_VIA");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 判断网络请求是否为本机IP
     *
     * @param request 网络请求信息
     * @return 是否为本机IP
     */
    public static boolean validLocalIP(HttpServletRequest request) {
        String ip = HttpUtil.getIP(request);

        return StringUtils.equals("127.0.0.1", ip) || StringUtils.equals("localhost", ip) || StringUtils.equals("0:0:0:0:0:0:0:1", ip);
    }

    /**
     * 尝试连接URL
     *
     * @param address 连接地址
     * @return 是否成功
     */
    public static boolean tryConnect(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            return urlConnection.getResponseCode() == HttpStatus.OK.value();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
