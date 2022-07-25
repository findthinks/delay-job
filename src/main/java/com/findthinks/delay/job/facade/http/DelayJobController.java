package com.findthinks.delay.job.facade.http;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.lib.result.FoxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.*;

@RestController
@RequestMapping(value = API_PREFIX)
public class DelayJobController {

    private static final Logger LOG = LoggerFactory.getLogger(DelayJobController.class);

    @Resource
    private JobScheduler jobScheduler;

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
    public FoxResult submitJobs(@RequestBody FacadeJob job) {
        try {
            jobScheduler.cancelJob(job.getOutJobNo());
            return FoxResult.SUCCESS;
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
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
