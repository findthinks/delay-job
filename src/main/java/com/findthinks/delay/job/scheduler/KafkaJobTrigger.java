package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@ConditionalOnProperty(prefix = "spring.kafka", name = "bootstrap-servers")
public class KafkaJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaJobTrigger.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public TriggerResult trigger(Job job) {
        TriggerResult result = TriggerResult.SUCCESS;
        CallbackMsg msg = CallbackMsg.create(job);
        try {
            SendResult<String, Object> sr = kafkaTemplate.send(job.getCallbackEndpoint(), msg).get();// 阻塞获取结果，成功获取则触发成功，是否优化为异步？？
        } catch (Exception ee) {
            LOG.error("Send notify message to kafka error.", ee);
            result = new TriggerResult("fail", "Send notify message to kafka error.");
        }
        return result;
    }

    private static class CallbackMsg {
        private String outJobNo;

        private Long triggerTime;

        private String jobInfo;

        public CallbackMsg(String outJobNo, Long triggerTime, String jobInfo) {
            this.outJobNo = outJobNo;
            this.triggerTime = triggerTime;
            this.jobInfo = jobInfo;
        }

        public static CallbackMsg create(Job job) {
            return new CallbackMsg(job.getOutJobNo(), job.getTriggerTime(), job.getJobInfo());
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
