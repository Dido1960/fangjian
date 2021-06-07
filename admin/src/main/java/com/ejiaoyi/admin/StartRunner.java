package com.ejiaoyi.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.admin.service.IOldProjectService;
import com.ejiaoyi.admin.service.impl.QuartzServiceImpl;
import com.ejiaoyi.common.entity.BidderQuantity;
import com.ejiaoyi.common.entity.QuartzJob;
import com.ejiaoyi.common.enums.QuartzState;
import com.ejiaoyi.common.service.impl.BidderQuantityServiceImpl;
import com.ejiaoyi.common.service.impl.WordbookServiceImpl;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 项目启动任务
 *
 * @author Z0001
 * @since 2020-5-9
 */
@Component
public class StartRunner implements CommandLineRunner {

    @Value("${spring.cache.redis.key-prefix}")
    private String CACHE_PREFIX;

    @Autowired
    private WordbookServiceImpl wordbookService;

    @Autowired
    private QuartzServiceImpl quartzService;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private BidderQuantityServiceImpl bidderQuantityService;

    /*@Autowired
    private IOldProjectService oldProjectService;*/

    @Override
    public void run(String... args) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> start runner >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 系统启动逻辑
        // 设置项目路劲
        FileUtil.setRootPath(System.getProperty("user.dir"));

        // 初始化字段表
        wordbookService.initWordBook();

        this.delRedisCaches();

        this.restoreQuantityJob();

        this.initQuartzJobs();
        //oldProjectService.addOldProject("C:\\Users\\LuoJiang\\Desktop\\temp", 1);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> end start runner >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }


    /**
     * 初始化Quartz定时任务
     */
    private void initQuartzJobs() {
        try {
            List<QuartzJob> jobs = quartzService.listQuartzJobEnabled();
            for(QuartzJob job : jobs) {
                quartzService.schedulerJob(job);
                if (QuartzState.PAUSED.getState().equals(job.getTriggerState())) {
                    scheduler.pauseJob(new JobKey(job.getName(), job.getJobGroup()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除Redis 缓存
     */
    private void delRedisCaches() {
        RedisConnection connection = RedisUtil.redisTemplate.getConnectionFactory().getConnection();

        Set<byte[]> caches = connection.keys(CACHE_PREFIX.getBytes());
        if(CollectionUtils.isNotEmpty(caches)){
            connection.del(caches.toArray(new byte[][]{}));
        }
    }

    /**
     * 针对BidderQuantity表中flag等于2的服务 在启动时 将状态重置为0 方便QuartzJob进行自动处理
     */
    private void restoreQuantityJob() {
        // 恢复错漏项分析服务
        QueryWrapper<BidderQuantity> bidderQuantityQueryWrapper = new QueryWrapper<BidderQuantity>()
                .eq("STRUCTURE_ANALYSIS_FLAG", 2);

        List<BidderQuantity> bidderQuantityList = bidderQuantityService.list(bidderQuantityQueryWrapper);

        if (CollectionUtils.isNotEmpty(bidderQuantityList)) {
            for (BidderQuantity bidderQuantity : bidderQuantityList) {
                bidderQuantity.setStructureAnalysisFlag(0);
            }

            bidderQuantityService.updateBatchById(bidderQuantityList);
        }

        // 恢复算术性分析服务
        bidderQuantityQueryWrapper = new QueryWrapper<BidderQuantity>()
                .eq("ARITHMETIC_ANALYSIS_FLAG", 2);

        bidderQuantityList = bidderQuantityService.list(bidderQuantityQueryWrapper);

        if (CollectionUtils.isNotEmpty(bidderQuantityList)) {
            for (BidderQuantity bidderQuantity : bidderQuantityList) {
                bidderQuantity.setArithmeticAnalysisFlag(0);
            }

            bidderQuantityService.updateBatchById(bidderQuantityList);
        }

        // 恢复零负报价分析服务
        bidderQuantityQueryWrapper = new QueryWrapper<BidderQuantity>()
                .eq("PRICE_ANALYSIS_FLAG", 2);

        bidderQuantityList = bidderQuantityService.list(bidderQuantityQueryWrapper);

        if (CollectionUtils.isNotEmpty(bidderQuantityList)) {
            for (BidderQuantity bidderQuantity : bidderQuantityList) {
                bidderQuantity.setPriceAnalysisFlag(0);
            }

            bidderQuantityService.updateBatchById(bidderQuantityList);
        }

        // 恢复取费基础分析服务
        bidderQuantityQueryWrapper = new QueryWrapper<BidderQuantity>()
                .eq("RULE_ANALYSIS_FLAG", 2);

        bidderQuantityList = bidderQuantityService.list(bidderQuantityQueryWrapper);

        if (CollectionUtils.isNotEmpty(bidderQuantityList)) {
            for (BidderQuantity bidderQuantity : bidderQuantityList) {
                bidderQuantity.setRuleAnalysisFlag(0);
            }

            bidderQuantityService.updateBatchById(bidderQuantityList);
        }
    }
}
