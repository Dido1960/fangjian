package com.ejiaoyi.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 应用上下文管理工具类
 *
 * @author Z0001
 * @since 2020-4-23
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextUtil.applicationContext;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name Bean name
     * @return Bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz Bean clazz
     * @param <T>   Bean T
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  Bean name
     * @param clazz Bean clazz
     * @param <T>   Bean T
     * @return Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
