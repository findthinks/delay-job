package com.findthinks.delay.job.facade.http;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.*;

@RestController
public class DelayJobController {

    @Resource
    private JobScheduler jobScheduler;

    @PostMapping("/submit/job")
    public JobResponse submitJob(@RequestBody FacadeJob job) {
        try {
            jobScheduler.submitJob(job);
            return successResp();
        } catch (Exception ex) {
            return failResp(ex);
        }
    }

    @PostMapping("/submit/jobs")
    public JobResponse submitJobs(@RequestBody List<FacadeJob> jobs) {
        try {
            jobScheduler.submitJobs(jobs);
            return successResp();
        } catch (Exception ex) {
            return failResp(ex);
        }
    }

    @PostMapping("/cancel/job")
    public JobResponse submitJobs(@RequestBody FacadeJob job) {
        try {
            jobScheduler.cancelJob(job.getOutJobNo());
            return successResp();
        } catch (Exception ex) {
            return failResp(ex);
        }
    }

    private JobResponse successResp() {
        return new JobResponse(JOB_SUCCESS, JOB_SUCCESS_MSG);
    }

    private JobResponse failResp(Exception ex) {
        return new JobResponse(JOB_FAIL, JOB_FAIL_MSG);
    }

    public static class JobResponse {
        private Integer code;

        private String msg;

        public JobResponse(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
