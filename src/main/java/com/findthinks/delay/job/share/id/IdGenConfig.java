package com.findthinks.delay.job.share.id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class IdGenConfig {

    @Bean
    public SequenceKeyRepository sequenceKeyRepository(JdbcTemplate jdbcTemplate) {
        return new SequenceKeyRepository(jdbcTemplate);
    }

    @Bean
    public SequenceKeyService sequenceKeyService(SequenceKeyRepository sequenceKeyRepository) {
        return new SequenceKeyServiceImpl(sequenceKeyRepository);
    }

    @Bean
    public KeyGeneratorManager keyGeneratorManager(SequenceKeyService sequenceKeyService) {
        return new KeyGeneratorManager(sequenceKeyService);
    }
}