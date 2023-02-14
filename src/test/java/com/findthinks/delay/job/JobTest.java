package com.findthinks.delay.job;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.share.lib.utils.UUIDUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class JobTest {

    @Resource
    private JobScheduler jobScheduler;

    @Test
    public void testJob() {
        for (int j=0; j<1000; j++) {
            FacadeJob job = new FacadeJob();
            job.setTriggerTime(System.currentTimeMillis()/1000 + new Random().nextInt(1200));
            job.setJobInfo("I am a delay job.");
            job.setOutJobNo(UUIDUtils.randomUUID());
            job.setType(5);
            job.setCallbackEndpoint("LOG");
            job.setCallbackProtocol("LOG");
            jobScheduler.submitJob(job);
        }
    }

    @Test
    public void testJob2() {
        for (int i=0; i<100; i++) {
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

    @Test
    public void testJob3() {
        for (int i=0; i<500; i++) {
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
}
