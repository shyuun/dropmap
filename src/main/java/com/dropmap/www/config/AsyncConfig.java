package com.dropmap.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);      // 동시에 실행할 최대 스레드 수
        executor.setMaxPoolSize(20);       // 최대 확장 가능 수
        executor.setQueueCapacity(1000);   // 대기열 수용량
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
