package com.findthinks.delay.job.core.delay;

import com.alibaba.fastjson.JSONObject;
import com.findthinks.delay.job.core.repository.entity.Job;
import com.findthinks.delay.job.share.enums.ExceptionEnum;
import com.findthinks.delay.job.share.exception.DelayJobException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;

@Component("httpJobTrigger")
public class HttpJobTrigger implements IJobTrigger {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public TriggerResult triggerJob(Job job) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CallbackInfo cb = new CallbackInfo(job.getOutJobNo(), job.getTriggerTime(), job.getJobInfo());
        HttpEntity<CallbackInfo> req = new HttpEntity<>(cb, headers);
        ResponseEntity<JSONObject> ret = restTemplate.postForEntity(job.getCallbackEndpoint(), req, JSONObject.class);
        if (ret.getStatusCode().is2xxSuccessful()) {
            JSONObject body = ret.getBody();
            return new TriggerResult(body.getInteger("code"), body.getString("msg"));
        }
        throw  new DelayJobException(ExceptionEnum.UNKNOWN_ERROR, "Callback response error, http_status: " + ret.getStatusCodeValue());
    }

    private class CallbackInfo {
        private String outJobNo;

        private Long triggerTime;

        private String jobInfo;

        public CallbackInfo(String outJobNo, Long triggerTime, String jobInfo) {
            this.outJobNo = outJobNo;
            this.triggerTime = triggerTime;
            this.jobInfo = jobInfo;
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
}
