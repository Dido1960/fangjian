package com.ejiaoyi.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池配置类
 *
 * @author unknownChivalrous
 * @since 2020-4-10
 */
public class AsyncTaskConfig implements AsyncConfigurer {

    /**
     * 线程名称前缀
     */
    private static final String THREAD_NAME_PREFIX = "ejiaoyi_EXEUTOR_";

    /**
     * 核心线程池大小
     */
    @Value("${task.pool.core-pool-size}")
    private int corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${task.pool.max-pool-size}")
    private int maxPoolSize;

    /**
     * 活跃时间
     */
    @Value("${task.pool.keep-alive-seconds}")
    private int keepAliveSeconds;

    /**
     * 队列容量
     */
    @Value("${task.pool.queue-capacity}")
    private int queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
