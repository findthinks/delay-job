package com.findthinks.delay.job.facade.grpc.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ServerConfig {

    @Value("${grpc.executor.num:50}")
    private int grpcExecutorNum;

    @Bean
    public GrpcServerConfigurer keepAliveServerConfigurer() {
        return serverBuilder -> {
            Executor grpcExecutor = Executors.newFixedThreadPool(grpcExecutorNum,
                    new ThreadFactoryBuilder().setNameFormat("grpc-executor-%d").build());
            serverBuilder.executor(grpcExecutor);
        };
    }
}
