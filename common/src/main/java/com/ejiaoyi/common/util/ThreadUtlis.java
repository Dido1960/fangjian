package com.ejiaoyi.common.util;


import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/***
 * 线程运行工具类
 * **
 * **/
public class ThreadUtlis {

    private static boolean initialized = false;
    
    private static int num = 5;

    static {
        try {
            Environment env = ApplicationContextUtil.getApplicationContext().getBean(Environment.class);
            num = Integer.parseInt(env.getProperty("task.pool.thread-utlis-max-size"));
        } catch (Exception e) {
            num = 5;
        }
    }

    //禁止反射破坏单例模式
    private ThreadUtlis() {
        synchronized (ThreadUtlis.class) {
            if (initialized == false) {
                initialized = !initialized;
            } else {
                throw new RuntimeException("单例已被破坏");
            }
        }
    }

    static class SingletonHolder {
        private static  final ExecutorService instance= Executors.newFixedThreadPool(num);
    }

    /***
     * 获取线程池对象
     * ***/
    public static ExecutorService getInstance() {
        return SingletonHolder.instance;
    }
    
    /**
     * 运行
     * **/
    public static void run(Runnable e){
        getInstance().execute( e);
    }
}
