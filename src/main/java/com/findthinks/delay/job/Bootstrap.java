package com.findthinks.delay.job;

import com.findthinks.delay.job.share.repository.mapper.MapperLocation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author YuBo
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackageClasses = MapperLocation.class)
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class);
    }
}