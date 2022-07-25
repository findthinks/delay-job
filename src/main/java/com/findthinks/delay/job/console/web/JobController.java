package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobInfoReq;
import com.findthinks.delay.job.console.web.rr.JobInfoResp;
import com.findthinks.delay.job.scheduler.IJobManager;
import com.findthinks.delay.job.share.repository.entity.Job;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX)
public class JobController {

    @Resource
    private IJobManager jobManager;

    @PostMapping(value = "/job/detail")
    public JobInfoResp getJobInfo(JobInfoReq req) {
        Job job = jobManager.loadJob(req.getOutJobNo());
        return null == job ? null: convert(job);
    }

    private JobInfoResp convert(Job job) {
        JobInfoResp resp = new JobInfoResp();
        resp.setGmtCreate(job.getGmtCreate());
        resp.setJobShardId(job.getJobShardId());
        resp.setOutJobNo(job.getOutJobNo());
        resp.setRetryTimes(job.getRetryTimes());
        resp.setState(job.getState());
        resp.setCallbackEndpoint(job.getCallbackEndpoint());
        resp.setCallbackProtocol(job.getCallbackProtocol());
        resp.setJobInfo(job.getJobInfo());
        resp.setTriggerTime(job.getTriggerTime());
        resp.setId(job.getId());
        return resp;
    }
}