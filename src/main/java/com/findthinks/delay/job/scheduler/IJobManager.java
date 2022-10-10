package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.GlobalRec;
import com.findthinks.delay.job.share.repository.entity.Job;
import java.util.List;
import java.util.Map;

public interface IJobManager {

    List<List<Job>> loadRecentlyJobs(List<Integer> jobShardIds, Long nextTriggerTime, Integer maxJobNums);

    List<Job> loadJobs(Integer jobShardId, Long nextTriggerTime, Integer maxJobs);

    List<Job> loadShardJobs(Integer jobShardId, Long nextTriggerTime, Integer maxJobs);

    Job getOneUnSuccessJob(Integer jobShardId, long triggerTimeStart, long triggerTimeEnd);

    boolean modifyJobState(Job job, int newState, int oldState, int retryTimes);

    void modifyJobState(int jobShardId, List<Job> jobs, int newState);

    List<Job> loadJobsInternal(Integer jobShardId, Long triggerTimeStart, Long triggerTimeEnd, Integer maxJobs);

    int createJob(Job job);

    void createJobs(Map<Integer, List<Job>> jobs, List<GlobalRec> recs);

    Job loadJob(int jobShardId, String outTaskNo);

    Job loadJob(int jobShardId, long jobId);

    Job loadJob(String outTaskNo);

    boolean pause(String outJobNo);

    Job resume(String outJobNo);

    List<Job> loadRetryJobs(Integer jobShardId, Long startTime, Long endTime, Integer maxJobs);

    int deleteShardJobs(Integer jobShardId, List<Long> jobIds);
}