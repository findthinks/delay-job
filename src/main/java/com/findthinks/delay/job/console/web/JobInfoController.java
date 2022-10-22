package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobInfoResp;
import com.findthinks.delay.job.scheduler.IJobManager;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.repository.entity.Job;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX)
@Validated
public class JobInfoController {

    @Resource
    private IJobManager jobManager;

    @Resource
    private JobScheduler jobScheduler;

    @GetMapping(value = "/job/{outJobNo}")
    public List<JobInfoResp> getJobInfo(
            @PathVariable("outJobNo")
            @Size(max = 32, min = 1, message = "任务编号所含字符数在[1，32]的范围内") String outJobNo) {
        Job job = jobManager.loadJob(outJobNo);
        return null == job ? null: Arrays.asList(convert(job));
    }

    @PutMapping(value = "/job/{outJobNo}/cancel")
    public void cancelJob(
            @PathVariable("outJobNo")
            @Size(max = 32, min = 1, message = "任务编号所含字符数在[1，32]的范围内") String outJobNo) {
        jobScheduler.cancelJob(outJobNo);
    }

    private JobInfoResp convert(Job job) {
        JobInfoResp resp = new JobInfoResp();
        resp.setGmtCreate(job.getGmtCreate());
        resp.setJobShardId(job.getJobShardId());
        resp.setOutJobNo(job.getOutJobNo());
        resp.setRetryTimes(job.getRetryTimes());
        resp.setState(job.getState());
        resp.setType(job.getType());
        resp.setCallbackEndpoint(job.getCallbackEndpoint());
        resp.setCallbackProtocol(job.getCallbackProtocol());
        resp.setJobInfo(job.getJobInfo());
        resp.setTriggerTime(job.getTriggerTime());
        resp.setId(job.getId());
        return resp;
    }
}