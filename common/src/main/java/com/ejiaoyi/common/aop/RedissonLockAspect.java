package com.ejiaoyi.common.aop;

import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.Constants;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁/防重复提交 aop
 *
 * @author Make
 */
@Aspect
@Component
@Slf4j
public class RedissonLockAspect {

    /**
     * SpEL 解析器
     */
    private ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * 将方法参数纳入Spring管理
     */
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 切点，拦截被 @RedissonLockAnnotation 修饰的方法
     */
    @Pointcut("@annotation(com.ejiaoyi.common.annotation.RedissonLock)")
    public void redissonLockPoint() {
    }

    @Around("redissonLockPoint()")
    public Object checkLock(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //当前线程名
        String threadName = Thread.currentThread().getName();

        //获取参数列表
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Object target = proceedingJoinPoint.getTarget();

        //获取该注解的实例对象
        RedissonLock annotation = ((MethodSignature) proceedingJoinPoint.getSignature()).
                getMethod().getAnnotation(RedissonLock.class);
        String SpEL = annotation.key();
        String key = target.getClass().getSimpleName() + "_" + method.getName();
        if (StringUtils.isEmpty(SpEL)) {
            log.info("线程{} SpEL设置为空，不加锁", threadName);
            RedissonUtil.lock(key);
            log.info("线程{} 获取锁成功", threadName);
            Object object;
            try {
                object = proceedingJoinPoint.proceed();
                return object;
            } finally {
                RedissonUtil.unlock(key);
                log.debug("线程{} 释放锁成功", threadName);
            }
        }
        EvaluationContext context = new StandardEvaluationContext();
        String[] params = discoverer.getParameterNames(method);
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
            log.error("RedissonLock中定义的值，不符合SPEL语法！！！！！！" + annotation.key());
            log.error("错误的类,方法名称");
            log.error(target.getClass().getSimpleName() + "-->" + method.getName());
            throw e;
        }

        String content = expression.getValue(context, String.class);
            key += "_" + content;
        //获取锁
        if (RedissonUtil.tryLock(key, Constants.REDISSON_LOCK_WAIT_TIME, Constants.REDISSON_LOCK_LEASE_TIME)) {
            log.debug("线程{}------进入分布式锁aop------", threadName);
            Object object;
            try {
                object = proceedingJoinPoint.proceed();
                return object;
            } finally {
                RedissonUtil.unlock(key);
                log.debug("线程{} 释放锁成功", threadName);
            }
        } else {
            log.error("线程{} 获取锁失败", threadName);
            return null;
        }
    }
}