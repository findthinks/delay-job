package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;

@Component("httpJobTrigger")
public class HttpJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(HttpJobTrigger.class);

    @Resource
    private RestTemplate restTemplate;

    @Override
    public TriggerResult trigger(Job job) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallbackInfo> req = new HttpEntity<>(CallbackInfo.create(job), headers);
        ResponseEntity<JSONObject> ret = restTemplate.postForEntity(job.getCallbackEndpoint(), req, JSONObject.class);
        if (ret.getStatusCode().is2xxSuccessful()) {
            JSONObject body = ret.getBody();
            try {
                return new TriggerResult(body.getString("code"), body.getString("msg"));
            } catch (JSONException e) {
                LOG.error("Parse callback result error.", e);
                throw new DelayJobException(ExceptionEnum.UNKNOWN_ERROR, "Callback response error, http_status: " + ret.getStatusCodeValue());
            }
        }
        throw new DelayJobException(ExceptionEnum.UNKNOWN_ERROR, "Callback response error, http_status: " + ret.getStatusCodeValue());
    }

    private static class CallbackInfo {
        private String outJobNo;

        private Long triggerTime;

        private String jobInfo;

        public CallbackInfo(String outJobNo, Long triggerTime, String jobInfo) {
            this.outJobNo = outJobNo;
            this.triggerTime = triggerTime;
            this.jobInfo = jobInfo;
        }

        public static CallbackInfo create(Job job) {
            return new CallbackInfo(job.getOutJobNo(), job.getTriggerTime(), job.getJobInfo());
        }

        public String getOutJobNo() {
            return outJobNo;
        }

        public void setOutJobNo(String outJobNo) {
            this.outJobNo = outJobNo;
        }

        public Long getTriggerTime() {
            return triggerTime;
        }

        public void setTriggerTime(Long triggerTime) {
            this.triggerTime = triggerTime;
        }

        public String getJobInfo() {
            return jobInfo;
        }

        public void setJobInfo(String jobInfo) {
            this.jobInfo = jobInfo;
        }
    }

    @Configuration
    public class HttpJobTriggerConfig {
        @Value("${scheduler.job.http-trigger-connect-timeout:1}")
        private int connectTimeout;
        @Value("${scheduler.job.http-trigger-read-timeout:1}")
        private int readTimeout;

        @Bean
        public RestTemplate restTemplate() {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout * 1000);
            requestFactory.setReadTimeout(readTimeout * 1000);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(requestFactory);
            return restTemplate;
        }
    }
}
