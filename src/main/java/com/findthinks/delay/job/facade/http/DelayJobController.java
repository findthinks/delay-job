package com.findthinks.delay.job.facade.http;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.lib.result.FoxResult;
import com.findthinks.delay.job.share.lib.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.*;

@RestController
@RequestMapping(value = API_PREFIX)
public class DelayJobController {

    private static final Logger LOG = LoggerFactory.getLogger(DelayJobController.class);

    @Resource
    private JobScheduler jobScheduler;

    @GetMapping(value = "/test/job")
    public void test() {
        for (int j=0; j<10000; j++) {
            FacadeJob job = new FacadeJob();
            job.setTriggerTime(System.currentTimeMillis()/1000 + new Random().nextInt(40));
            job.setJobInfo("I am a delay job.");
            job.setOutJobNo(UUIDUtils.randomUUID());
            job.setType(5);
            job.setCallbackEndpoint("LOG");
            job.setCallbackProtocol("LOG");
            jobScheduler.submitJob(job);
        }
    }

    @GetMapping(value = "/test/job2/{batch}/{triggerTime}")
    public void test2(@PathVariable("batch") int batch, @PathVariable("triggerTime") long triggerTime) {
        for (int i=0; i<batch; i++) {
            List<FacadeJob> jobs = new ArrayList<>(100);
            for (int j=0; j<100; j++) {
                FacadeJob job = new FacadeJob();
                job.setTriggerTime(System.currentTimeMillis()/1000 + new Random().nextInt(55));
                job.setJobInfo("I am a delay job.");
                job.setOutJobNo(UUIDUtils.randomUUID());
                job.setType(5);
                job.setCallbackEndpoint("LOG");
                job.setCallbackProtocol("LOG");
                jobs.add(job);
            }
            jobScheduler.submitJobs(jobs);
        }
    }

    @GetMapping(value = "/test/job3/{batch}/{triggerTime}")
    public void test3(@PathVariable("batch") int batch, @PathVariable("triggerTime") long triggerTime) {
        for (int i=0; i<batch; i++) {
            List<FacadeJob> jobs = new ArrayList<>(100);
            for (int j=0; j<100; j++) {
                FacadeJob job = new FacadeJob();
                job.setTriggerTime(System.currentTimeMillis()/1000 + new Random().nextInt(300));
                job.setJobInfo("I am a delay job.");
                job.setType(5);
                job.setOutJobNo(UUIDUtils.randomUUID());
                job.setCallbackEndpoint("LOG");
                job.setCallbackProtocol("LOG");
                jobs.add(job);
            }
            jobScheduler.submitJobs(jobs);
        }
    }

    @PostMapping("/submit/job")
    public FoxResult submitJob(@RequestBody FacadeJob job) {
        try {
            jobScheduler.submitJob(job);
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Submit delay job error.", ex);
            return wrapErrorResult(ex);
        }
    }

    @PostMapping("/submit/jobs")
    public FoxResult submitJobs(@RequestBody List<FacadeJob> jobs) {
        try {
            jobScheduler.submitJobs(jobs);
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Submit delay jobs error.", ex);
            return wrapErrorResult(ex);
        }
    }

    @PostMapping("/cancel/job")
    public FoxResult cancelJob(@RequestBody FacadeJob job) {
        try {
            jobScheduler.cancelJob(job.getOutJobNo());
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
            return wrapErrorResult(ex);
        }
    }

    @PostMapping("/pause/job")
    public FoxResult pauseJob(@RequestBody FacadeJob job) {
        try {
            jobScheduler.pauseJob(job.getOutJobNo());
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Pause delay job error.", ex);
            return wrapErrorResult(ex);
        }
    }

    @PostMapping("/resume/job")
    public FoxResult resumeJob(@RequestBody FacadeJob job) {
        try {
            jobScheduler.resumeJob(job.getOutJobNo());
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Resume delay job error.", ex);
            return wrapErrorResult(ex);
        }
    }

    public FoxResult wrapErrorResult(Exception ex) {
        if (ex instanceof DelayJobException) {
            DelayJobException err = (DelayJobException) ex;
            return FoxResult.fail(err.getExceptionEnum(), ex.getMessage());
        }
        return FoxResult.UNKNOWN_ERROR;
    }
}
