package com.findthinks.delay.job.console.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.findthinks.delay.job.console.plugin.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${static.resources.dir:classpath:static/}")
    private String frontendBaseDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/v1/login",
                        "/api/v1/logout",
                        "/api/v1/submit/job",
                        "/api/v1/submit/jobs",
                        "/api/v1/pause/job",
                        "/api/v1/resume/job",
                        "/api/v1/cancel/job");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(frontendBaseDir);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(frontendBaseDir);
        registry.viewResolver(viewResolver);
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}