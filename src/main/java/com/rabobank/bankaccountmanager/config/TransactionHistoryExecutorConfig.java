package com.rabobank.bankaccountmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TransactionHistoryExecutorConfig {

    @Value(value = "${transaction.history.executor.service.pool.size}")
    private int executorPoolSize;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(executorPoolSize);
    }
}
