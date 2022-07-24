package com.findthinks.delay.job.console.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YuBo
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //处理actuator响应序列化
        MappingJackson2HttpMessageConverter indicatorConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        indicatorConverter.setObjectMapper(objectMapper);
        List<MediaType> indicatorMediaTypes = new ArrayList<>();
        indicatorMediaTypes.add(new MediaType("application", "vnd.spring-boot.actuator.v2+json"));
        indicatorConverter.setSupportedMediaTypes(indicatorMediaTypes);
        converters.add(indicatorConverter);
    }
}