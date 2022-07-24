package com.findthinks.delay.job;

import com.findthinks.delay.job.share.repository.mapper.MapperLocation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author YuBo
 */
@SpringBootApplication
@MapperScan(basePackageClasses = MapperLocation.class)
public class DelayJobBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(DelayJobBootstrap.class).web(WebApplicationType.SERVLET).run(args);
    }
}