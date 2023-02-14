package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.repository.entity.GlobalRec;
import com.findthinks.delay.job.share.repository.entity.Job;
import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import com.findthinks.delay.job.share.repository.mapper.GlobalRecExtMapper;
import com.findthinks.delay.job.share.repository.mapper.JobExtMapper;
import com.findthinks.delay.job.share.repository.mapper.JobSegTriggerExtMapper;
import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.V_CPU_CORES;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.*;

@Service
public class JobManager implements IJobManager {

    private static final Logger LOG = LoggerFactory.getLogger(JobManager.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(V_CPU_CORES, new ThreadFactoryBuilder().setNameFormat("Load-Job-Executor-%d").setDaemon(true).build());

    @Resource
    private IJobSegTriggerFlowManager jobSegTriggerFlowManager;

    @Resource
    private JobSegTriggerExtMapper jobSegTriggerExtMapper;

    @Resource
    private JobExtMapper jobExtMapper;

    @Resource
    private GlobalRecExtMapper globalRecExtMapper;

    @Resource
    private IGlobalRecManager globalRecManager;

    @Override
    public List<List<Job>> loadRecentlyJobs(List<Integer> jobShardIds, Long nextTriggerTime, Integer maxJobNums) {
        final List<List<Job>> delayJobs = new ArrayList<>(jobShardIds.size());
        try {
            this.executor.invokeAll(doLoadDelayJob(jobShardIds, nextTriggerTime, maxJobNums)).forEach(futureJobs -> {
                try {
                    if (!CollectionUtils.isEmpty(futureJobs.get())) {
                        delayJobs.add(futureJobs.get());
                    }
                } catch (Exception ignore) {
                    LOG.info("Warning load recently job error.", ignore);
                }
            });
        } catch (InterruptedException ignore) {
            LOG.info("Warning load recently job error.", ignore);
        }
        return delayJobs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Job> loadJobs(Integer jobShardId, Long nextTriggerTime, Integer maxJobs) {
        try {
            for (;;) {
                JobSegTrigger seg = loadJobSegTrigger(jobShardId);
                if (null == seg) {
                    initJobSegTrigger(jobShardId);
                } else if (compareAndSet(seg.getJobShardId(), seg.getTriggerTimeStart(), seg.getTriggerTimeEnd(), nextTriggerTime)) {
                    LOG.info("Load job segment: {} ~ {}", seg.getTriggerTimeEnd(), nextTriggerTime);
                    //记录任务段明细
                    createJobSegTriggerFlow(jobShardId, seg.getTriggerTimeEnd(), nextTriggerTime);

                    //加载任务记录
                    return loadJobsInternal(jobShardId, seg.getTriggerTimeEnd(), nextTriggerTime, maxJobs);
                }
            }
        } catch (Exception ex) {
            LOG.error("Load job segment error.", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Job> loadShardJobs(Integer jobShardId, Long timeStart, Integer maxJobs) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("jobShardId", jobShardId);
        params.put("timeStart", timeStart);
        params.put("maxJobs", maxJobs);
        List<Job> jobs = jobExtMapper.selectShardJobs(params);
        return CollectionUtils.isEmpty(jobs) ? Collections.EMPTY_LIST : jobs;
    }

    @Override
    public Job getOneUnSuccessJob(Integer jobShardId, long triggerTimeStart, long triggerTimeEnd) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jobShardId", jobShardId);
        parameters.put("triggerTimeStart", triggerTimeStart);
        parameters.put("triggerTimeEnd", triggerTimeEnd);
        List<Job> jobs = jobExtMapper.selectOneUnSuccessJob(parameters);
        return CollectionUtils.isEmpty(jobs) ? null : jobs.get(0);
    }

    @Override
    public boolean modifyJobState(Job job, int newState, int oldState, int retryTimes) {
        Map<String, Object> parameters = new HashMap<>(8);
        parameters.put("id", job.getId());
        parameters.put("jobShardId", job.getJobShardId());
        parameters.put("newState", newState);
        parameters.put("oldState", oldState);
        parameters.put("retryTimes", retryTimes);
        return jobExtMapper.updateJobState(parameters) == 1;
    }

    @Override
    public void modifyJobState(int jobShardId, List<Job> jobs, int newState) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", jobShardId);
        parameters.put("newState", newState);
        parameters.put("ids", jobs.stream().map(job -> job.getId()).collect(Collectors.toList()));
        jobExtMapper.updateJobsState(parameters);
    }

    @Override
    public List<Job> loadJobsInternal(Integer jobShardId, Long triggerTimeStart, Long triggerTimeEnd, Integer maxJobs) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("jobShardId", jobShardId);
        params.put("triggerTimeStart", triggerTimeStart);
        params.put("triggerTimeEnd", triggerTimeEnd);
        params.put("maxJobs", maxJobs);
        List<Job> jobs = jobExtMapper.selectSubmitJobs(params);
        return CollectionUtils.isEmpty(jobs) ? Collections.EMPTY_LIST : jobs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createJob(Job job) {
        try {
            int effect = jobExtMapper.insertJob(job);

            //查询全局信息记录
            GlobalRec rec = new GlobalRec();
            rec.setOutJobNo(job.getOutJobNo());
            rec.setJobShardId(job.getJobShardId());
            rec.setTriggerTime(job.getTriggerTime());
            rec.setJobId(job.getId());
            rec.setGmtCreate(new Date());
            globalRecExtMapper.insertRec(rec);
            return effect;
        } catch (Exception ex) {
            if (ex instanceof DuplicateKeyException) {
                throw new DelayJobException(OUT_JOB_NO_IS_EXIST, "OutJobNo:" + job.getOutJobNo() + " duplicate!");
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJobs(Map<Integer, List<Job>> jobs, List<GlobalRec> recs) {
        jobs.entrySet().forEach(entry -> {
            if (!CollectionUtils.isEmpty(entry.getValue())) {
                jobExtMapper.insertBatchJobs(entry.getKey(), entry.getValue());
            }
        });
        globalRecExtMapper.insertRecs(recs);
    }

    @Override
    public Job loadJob(int jobShardId, String outTaskNo) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", jobShardId);
        parameters.put("outJobNo", outTaskNo);
        List<Job> selected = jobExtMapper.loadJob(parameters);
        return CollectionUtils.isEmpty(selected)? null : selected.get(0);
    }

    @Override
    public Job loadJob(int jobShardId, long jobId) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", jobShardId);
        parameters.put("jobId", jobId);
        List<Job> selected = jobExtMapper.loadJobById(parameters);
        return CollectionUtils.isEmpty(selected)? null : selected.get(0);
    }

    @Override
    public Job loadJob(String outJobNo) {
        GlobalRec rec = globalRecExtMapper.selectRecByOutJobNo(outJobNo);
        return null == rec ? null : loadJob(rec.getJobShardId(), outJobNo);
    }

    @Override
    public Job resume(String outJobNo) {
        GlobalRec rec = globalRecExtMapper.selectRecByOutJobNo(outJobNo);
        Job job = loadJob(rec.getJobShardId(), outJobNo);
        if (null == job) {
            throw new DelayJobException(JOB_NOT_EXIST);
        }

        if (JobType.NORMAL.getCode() == job.getType().intValue()) {
            throw new DelayJobException(INVALID_PARAMS, "Cannot resume normal job.");
        }

        // 计算最新触发时间
        long newTriggerTime = job.getTriggerTime() + (System.currentTimeMillis() - job.getPauseTime());
        job.setTriggerTime(newTriggerTime);
        job.setPauseTime(null);

        // 更新全局表trigger time
        globalRecManager.updateGlobalRecTriggerTime(rec.getId(), newTriggerTime);

        // 更新trigger time
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", job.getJobShardId());
        parameters.put("jobId", job.getId());
        parameters.put("triggerTime", newTriggerTime);
        jobExtMapper.updateJobTriggerTime(parameters);
        return job;
    }

    @Override
    public boolean pause(String outJobNo) {
        Job job = loadJob(outJobNo);
        if (null == job) {
            throw new DelayJobException(JOB_NOT_EXIST);
        }

        if (JobType.NORMAL.getCode() == job.getType().intValue()) {
            throw new DelayJobException(INVALID_PARAMS, "Cannot pause normal job.");
        }

        if (job.getTriggerTime() <= System.currentTimeMillis()) {
            throw new DelayJobException(JOB_IS_TRIGGERED);
        }

        //更新暂定计时点
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", job.getJobShardId());
        parameters.put("jobId", job.getId());
        parameters.put("pauseTime", System.currentTimeMillis());
        return 0 == jobExtMapper.updateJobPauseTime(parameters);
    }

    @Override
    public List<Job> loadRetryJobs(Integer jobShardId, Long startTime, Long endTime, Integer maxJobs) {
        List<Job> shardJob = loadNoneSuccessJobs(jobShardId, startTime, endTime, maxJobs);
        return CollectionUtils.isEmpty(shardJob) ? Collections.EMPTY_LIST : shardJob;
    }

    @Override
    public int deleteShardJobs(Integer jobShardId, List<Long> jobIds) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("jobShardId", jobShardId);
        parameters.put("ids", jobIds);
        return jobExtMapper.deleteJobs(parameters);
    }

    private List<LoadDelayJob> doLoadDelayJob(List<Integer> jobShardIds, Long nextTriggerTime, Integer maxJobNums) {
        final List<LoadDelayJob> jobs = new ArrayList<>(jobShardIds.size());
        jobShardIds.forEach(jobShardId -> jobs.add(new LoadDelayJob(jobShardId, nextTriggerTime, maxJobNums)));
        return jobs;
    }

    private void initJobSegTrigger(int jobShardId) {
        try {
            JobSegTrigger init = new JobSegTrigger();
            init.setJobShardId(jobShardId);
            init.setTriggerTimeStart(0L);
            init.setTriggerTimeEnd(0L);
            init.setGmtModify(new Date());
            jobSegTriggerExtMapper.insertSegTrigger(init);
        } catch (Exception ignore) {
            LOG.error("Initialize job segment trigger error, this error will be ignore.", ignore);
        }
    }

    private void createJobSegTriggerFlow(Integer jobShardId, Long timeStart, Long timeEnd) {
        //记录任务段明细
        JobSegTriggerFlow flow = new JobSegTriggerFlow();
        flow.setGmtCreate(new Date());
        flow.setJobShardId(jobShardId);
        flow.setState(TriggerFLowState.PROCESS.getCode());
        flow.setTriggerTimeStart(timeStart);
        flow.setTriggerTimeEnd(timeEnd);
        flow.setRetryTimes(0);
        jobSegTriggerFlowManager.insertSegTriggerFlow(flow);
    }

    private boolean compareAndSet(int jobShardId, long timeStart, long timeEnd, long newTimeEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("timeEnd", timeEnd);
        params.put("newTimeEnd", newTimeEnd);
        params.put("timeStart", timeStart);
        params.put("gmtModify", new Date());
        params.put("jobShardId", jobShardId);
        return jobSegTriggerExtMapper.compareAndSet(params) == 1;
    }

    private JobSegTrigger loadJobSegTrigger(int jobShardId) {
        return jobSegTriggerExtMapper.selectJobSegTriggerByShardId(jobShardId);
    }

    private List<Job> loadNoneSuccessJobs(Integer jobShardId, Long triggerTimeStart, Long triggerTimeEnd, Integer maxJobs) {
        Map<String, Object> params = new HashMap<>();
        params.put("jobShardId", jobShardId);
        params.put("triggerTimeEnd", triggerTimeEnd);
        params.put("triggerTimeStart", triggerTimeStart);
        params.put("maxJobs", maxJobs);
        return jobExtMapper.selectNoneSuccessJobs(params);
    }

    private final class LoadDelayJob implements Callable<List<Job>> {
        private final Integer jobShardId;
        private final Long nextTriggerTime;
        private final Integer maxJobNums;

        public LoadDelayJob(Integer jobShardId, Long nextTriggerTime, Integer maxJobNums) {
            this.jobShardId = jobShardId;
            this.nextTriggerTime = nextTriggerTime;
            this.maxJobNums = maxJobNums;
        }

        @Override
        public List<Job> call() {
            return loadJobs(jobShardId, nextTriggerTime, maxJobNums);
        }
    }
}