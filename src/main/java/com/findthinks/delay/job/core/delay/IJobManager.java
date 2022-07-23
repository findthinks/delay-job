package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.GlobalRec;
import com.findthinks.delay.job.core.repository.entity.Job;

import java.util.List;
import java.util.Map;

/**
 * @author YuBo
 */
public interface IJobManager {

    List<List<Job>> loadRecentlyJobs(List<Integer> jobShardIds, Long nextTriggerTime, Integer maxJobNums);

    List<Job> loadJobs(Integer jobShardId, Long nextTriggerTime, Integer maxJobs);

    List<Job> loadShardJobs(Integer jobShardId, Long nextTriggerTime, Integer maxJobs);

    boolean modifyJobState(Job job, int newState, int oldState, int retryTimes);

    void modifyJobState(int jobShardId, List<Job> jobs, int newState);

    List<Job> loadJobsInternal(Integer jobShardId, Long triggerTimeStart, Long triggerTimeEnd, Integer maxJobs);

    int createJob(Job job);

    void createJobs(Map<Integer, List<Job>> jobs, List<GlobalRec> recs);

    Job loadJob(int jobShardId, String outTaskNo);

    Job loadJob(String outTaskNo);

    List<Job> loadRetryJobs(Integer jobShardId, Long startTime, Long endTime, Integer maxJobs);

    int getNoneSuccessJobsCount(Integer jobShardId, long startTime, long endTime);

    int deleteShardJobs(Integer jobShardId, List<Long> jobIds);
}