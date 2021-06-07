package com.ejiaoyi.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.common.annotation.ApiAuthentication;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.ApiLog;
import com.ejiaoyi.common.enums.ApiCode;
import com.ejiaoyi.common.enums.DockApiCode;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.impl.ApiAuthServiceImpl;
import com.ejiaoyi.common.service.impl.ApiLogServiceImpl;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.DesUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * API AOP
 *
 * @author unknownChivalrous
 * @since 2020-03-17
 */
@Component
@Aspect
@Order(1)
public class ApiAspect {

    @Autowired
    private ApiLogServiceImpl apiLogService;

    @Autowired
    private ApiAuthServiceImpl apiAuthService;

    @Pointcut("@annotation(com.ejiaoyi.common.annotation.ApiAuthentication))")
    public void api() {

    }

    @Around("api()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Class<?> responseClass = method.getReturnType();
        ApiAuthentication apiAuthentication = method.getAnnotation(ApiAuthentication.class);

        String apiName = "";
        String platform = "";
        String apiKey = "";
        String methodName = proceedingJoinPoint.getSignature().getName();
        int authentication = apiAuthentication.authentication();
        apiName = apiAuthentication.apiName();
        if (authentication < 0 || StringUtils.isEmpty(apiName)) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS);
        }
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(args[authentication]));
        if (json == null) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS);
        }
        platform = json.getString("platform");
        apiKey = json.getString("api_key");

        boolean authFlag = apiAuthService.authentication(apiName, platform, apiKey);
        boolean backInfo = apiAuthService.authentication(apiName, apiKey, platform);
        if (backInfo) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS, "平台授权码与API_key位置反了", null);
        }

        if (!authFlag) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS);
        }

        String token = json.getString("token");
        if (apiAuthentication.replay()) {
            if (StringUtils.isEmpty(token)) {
                throw new APIException(DockApiCode.TOKEN_MUST);
            }
            if (!RedisUtil.hasKey(CacheName.TOKEN + token)) {
                throw new APIException(DockApiCode.TOKEN_INVALID);
            }
           /* String decrypt = DesUtil.decrypt(String.valueOf(RedisUtil.get(CacheName.TOKEN+token)));
            String[] arr = StringUtils.split(decrypt, "-");
            if (StringUtils.equals(apiKey, arr[0]) || StringUtils.equals(platform, arr[1])) {
                throw new APIException(DockApiCode.TOKEN_INVALID_ERROR);
            }*/
        }
        Object proceed = proceedingJoinPoint.proceed();
        try {
            RedisUtil.delete(CacheName.TOKEN + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proceed;
    }

    /**
     * 获取参数
     *
     * @param args 参数列表
     * @return 参数内容字符串
     */
    private String getParam(Object[] args) {
        if (args.length == 0) {
            return "";
        }

        StringBuilder paramStr = new StringBuilder();
        Class<?> clazz;
        String paramName;

        for (Object arg : args) {
            clazz = arg.getClass();
            paramName = clazz.getName();
            if (clazz.isPrimitive() || arg instanceof String) {
                paramStr.append(paramName)
                        .append("=")
                        .append(arg);
            } else {
                paramStr.append(paramName)
                        .append("=")
                        .append(JSONObject.toJSONString(arg));
            }
        }

        return paramStr.toString();
    }
}
