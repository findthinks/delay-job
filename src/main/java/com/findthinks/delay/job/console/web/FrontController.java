package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.share.id.KeyGenerator;
import com.findthinks.delay.job.share.id.KeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class FrontController {
    @Autowired
    private KeyGeneratorManager keyGeneratorManager;
    private static final Logger LOG = LoggerFactory.getLogger(FrontController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = {
            "/login",
            "/dashboard/**",
            "/dashboard/**",
            "/scheduler/**",
            "/shard/**",
            "/job/**",
            "/about/**",
            "/"})
    public String front() {
        return "forward:/index.html";
    }

    @RequestMapping("/test")
    public void test() {
        KeyGenerator keyGenerator = keyGeneratorManager.getKeyGenerator("JOB_ID");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> {
                StringBuilder valueSql = new StringBuilder();
                int count = 0;
                while (true) {
                    if (count <= 98)
                        valueSql.append("(" + keyGenerator.nextId() + "),");
                    if (count == 99) {
                        valueSql.append("(" + keyGenerator.nextId() + ");");
                        String sql = "INSERT INTO test values" + valueSql.toString();
                        try {
                            jdbcTemplate.execute(sql);
                        } catch (Throwable throwable) {
                            LOG.error("error", throwable);
                        }
                        count = 0;
                        valueSql = new StringBuilder();
                    }
                    count++;
                }
            });
        }
    }
}
