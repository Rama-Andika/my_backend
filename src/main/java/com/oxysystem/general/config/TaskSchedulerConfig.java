package com.oxysystem.general.config;

import com.oxysystem.general.scheduler.grab.grabFood.SplitOrderFoodScheduler;
import com.oxysystem.general.scheduler.grab.grabMart.SplitOrderMartScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class TaskSchedulerConfig {
    @Bean(name = "taskExecutor")
    @Primary
    public ThreadPoolTaskExecutor generalPurposeExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);           // Sesuai jumlah core fisik
        executor.setMaxPoolSize(6);            // Max 3x core
        executor.setQueueCapacity(50);         // Antrian sedang
        executor.setThreadNamePrefix("general-purpose-");
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean(name = "dashboardSummaryGrabExecutor")
    public ThreadPoolTaskExecutor dashboardSummaryGrabExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("dashboard-summary-grab-");
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean(name = "grabMenuSyncExecutor")
    public ThreadPoolTaskExecutor grabMenuSyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(6);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("GrabMenuSync-");
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean(destroyMethod = "dispose", name = "menuNotifyScheduler")
    public Scheduler menuNotifyScheduler() {
        return Schedulers.newBoundedElastic(
                20,                     // maksimum thread untuk parallel processing
                1000,      // antrian task
                "menu-notify-scheduler" // prefix thread name
        );
    }

    @Bean(destroyMethod = "dispose", name = "uploadSalesGrabMartScheduler")
    public Scheduler uploadSalesGrabMartScheduler() {
        return Schedulers.newBoundedElastic(
                20,                     // maksimum thread untuk parallel processing
                1000,      // antrian task
                "upload-sales-grab-mart-scheduler" // prefix thread name
        );
    }

    @Bean(destroyMethod = "dispose", name = "uploadSalesGrabFoodScheduler")
    public Scheduler uploadSalesGrabFoodScheduler() {
        return Schedulers.newBoundedElastic(
                20,                     // maksimum thread untuk parallel processing
                1000,      // antrian task
                "upload-sales-grab-food-scheduler" // prefix thread name
        );
    }

    @Bean(destroyMethod = "dispose", name = "splitOrderGrabScheduler")
    public Scheduler splitOrderGrabScheduler() {
        return Schedulers.newBoundedElastic(
                20,                     // maksimum thread untuk parallel processing
                1000,      // antrian task
                "split-order-grab-scheduler" // prefix thread name
        );
    }

    @Bean(name = "splitOrderGrabMartRateLimiter")
    public SplitOrderMartScheduler.RateLimiter grabMartRateLimiter(
            @Qualifier("splitOrderGrabScheduler") Scheduler scheduler) {
        return new SplitOrderMartScheduler.RateLimiter(scheduler);
    }

    @Bean(name = "splitOrderGrabFoodRateLimiter")
    public SplitOrderFoodScheduler.RateLimiter grabFoodRateLimiter(
            @Qualifier("splitOrderGrabScheduler") Scheduler scheduler) {
        return new SplitOrderFoodScheduler.RateLimiter(scheduler);
    }

}
