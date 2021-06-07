package com.ejiaoyi.common.aop;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.impl.UserLogServiceImpl;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * 用户日志记录
 *
 * @author Z0001
 * @since 2020-03-17
 */

@Component
@Aspect
@DS(DataSourceKey.LOG)
@Slf4j
public class UserLogAspect {
    /**
     * SpEL 解析器
     */
    private ExpressionParser expressionParser = new SpelExpressionParser();


    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

    /**
     * 将方法参数纳入Spring管理
     */
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.ejiaoyi.common.annotation.UserLog)")
    public void userLog() {
    }

    @AfterThrowing(pointcut = "userLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("EXCEPTION: " + e.getMessage());
    }

    @Around("userLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Object target = proceedingJoinPoint.getTarget();
        UserLog userLogAnnotation = method.getAnnotation(UserLog.class);

        String SpEL = userLogAnnotation.value();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        if (params != null) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], args[len]);
            }
        }
        Expression expression;
        try {
            expression = expressionParser.parseExpression(SpEL);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("userLog中定义的值，不符合SPEL语法！！！！！！" + userLogAnnotation.value() );
            log.error("错误的类,方法名称");
            log.error(target.getClass().getSimpleName() + "-->" + method.getName());
            throw e;
        }

        String content = expression.getValue(context, String.class);

        com.ejiaoyi.common.entity.UserLog userLog = com.ejiaoyi.common.entity.UserLog.builder()
                .content(content)
                .dmlType(userLogAnnotation.dmlType().getName())
                .createTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .build();

        // 当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if( authentication!=null){
            Map map= BeanUtil.beanToMap(authentication.getPrincipal());
            userLog.setUsername((String) map.get("name"));
            userLog.setUserId((Integer) map.get("userId"));
        }


        executorService.submit(() -> {
            UserLogServiceImpl userLogService = ApplicationContextUtil.getBean(UserLogServiceImpl.class);
            userLogService.addLog(userLog);
        });

        return proceedingJoinPoint.proceed();
    }
}
