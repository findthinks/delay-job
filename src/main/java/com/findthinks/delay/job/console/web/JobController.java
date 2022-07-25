package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobInfoResp;
import com.findthinks.delay.job.scheduler.IJobManager;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.repository.entity.Job;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX)
public class JobController {

    @Resource
    private IJobManager jobManager;

    @Resource
    private JobScheduler jobScheduler;

    @GetMapping(value = "/job/{outJobNo}")
    public JobInfoResp getJobInfo(@PathVariable("outJobNo") String outJobNo) {
        Job job = jobManager.loadJob(outJobNo);
        return null == job ? null: convert(job);
    }

    @PutMapping(value = "/job/{outJobNo}/cancel")
    public void cancelJob(@PathVariable("outJobNo") String outJobNo) {
        jobScheduler.cancelJob(outJobNo);
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