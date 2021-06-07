package com.ejiaoyi.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * href 提交的时候使用的工具类
 *
 * @author lesgod
 * @since 2020-4-26
 */
@Slf4j
public class HrefUtil {


    /***
     * 验证参数信息
     * **/
    public static boolean validation(HttpServletRequest httpServletRequest, String reqUri) {
        reqUri=reqUri.split("\\?")[0];
        HttpSession session = httpServletRequest.getSession();
        Map<String, String> map = new HashMap();
        Enumeration<String> enumeration = httpServletRequest.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if (!"url".equals(key)) {
                map.put(key, httpServletRequest.getParameter(key));
            }
        }
        if (map.size() > 0) {
            Map mapSession = JSONObject.parseObject((String) session.getAttribute("hrefSubmit_" + reqUri));
           if(mapSession==null){
               return true;
           }
            for (String key : map.keySet()) {
                if (!map.get(key).equals(mapSession.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }
}
