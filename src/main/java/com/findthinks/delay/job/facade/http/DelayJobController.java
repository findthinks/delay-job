package com.findthinks.delay.job.facade.http;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.lib.result.FoxResult;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.*;

@RestController
@RequestMapping(value = API_PREFIX)
@Validated
public class DelayJobController {

    private static final Logger LOG = LoggerFactory.getLogger(DelayJobController.class);

    @Resource
    private JobScheduler jobScheduler;

    @PostMapping("/submit/job")
    public FoxResult submitJob(@RequestBody @Valid FacadeJob job) {
        try {
            jobScheduler.submitJob(job);
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Submit delay job error.", ex);
            return wrapErrorResult(ex);
        }
    }

    @PostMapping("/submit/jobs")
    public FoxResult submitJobs(
            @RequestBody
            @Valid
            @Size(max = 100, min = 1, message = ":数组长度[1,100]的范围内") List<FacadeJob> jobs) {
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
        Assert.hasLength(job.getOutJobNo(), "outJobNo:不能为空，长度范围[1,32]");

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
        Assert.hasLength(job.getOutJobNo(), "outJobNo:不能为空，长度范围[1,32]");

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
        Assert.hasLength(job.getOutJobNo(), "outJobNo:不能为空，长度范围[1,32]");

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
