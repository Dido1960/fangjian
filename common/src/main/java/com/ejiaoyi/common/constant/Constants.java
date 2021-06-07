package com.ejiaoyi.common.constant;

/**
 * 通用常量集合
 *
 * @author Z0001
 * @since 2020/4/1
 */
public class Constants {

    public static final Integer ENABLED = 1;

    public static final Integer DISABLED = 0;

    public static final int LENGTH_REG_NO = 6;

    public static final int LENGTH_CREDIT_CODE = 18;

    public static final String TRUE = "true";

    public static final String FALSE = "false";

    /**
     * CACHE_NAME 默认的缓存时间 单位（天）
     * **/
    public  static  final  Integer CACHE_NAME_TIME = 2;


    /**
     * REDISSON_LOCK 最大等待时间 seconds
     */
    public  static  final  Integer REDISSON_LOCK_WAIT_TIME = 3000;

    /**
     * REDISSON_LOCK 上锁后的自动释放时间 seconds
     */
    public  static  final  Integer REDISSON_LOCK_LEASE_TIME = 5000;
}
