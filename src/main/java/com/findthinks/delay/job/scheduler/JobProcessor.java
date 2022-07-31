package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import com.findthinks.delay.job.share.repository.mapper.JobSegTriggerExtMapper;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.findthinks.delay.job.share.lib.constants.SystemConstants.V_CPU_CORES;

@Component
public class JobProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(JobProcessor.class);

    private static final int SCHEDULER_NUM = V_CPU_CORES;

    private static final int EXECUTOR_NUM = V_CPU_CORES * 4 + 1;

    private static final int DEFAULT_JOB_RETRY_TIMES = 3;

    private static final int JOB_STATE_UPDATE_BATCH_SIZE = 50;

    private static final int JOB_STATE_UPDATE_TIMEOUT = 1000;

    /** 延时调度器 */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_NUM, new ThreadFactoryBuilder().setNameFormat("Delay-Job-%d").setDaemon(true).build());

    /** 延迟执行处理器 */
    private final ExecutorService executor = new DelayThreadPoolExecutor(EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Executor-%d").setDaemon(true).build());

    /** 补偿处理器 */
    private final ExecutorService retryExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Job-Retry-%d").setDaemon(true).build());

    /** 异步更新数据库任务状态 */
    private final ExecutorService stateExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Job-State-Sync-%d").setDaemon(true).build());

    /** 收集已触发成功任务，批量更新数据库*/
    private final BlockingQueue<TriggeredJob> triggeredJobs = new LinkedBlockingQueue<>();

    @Resource
    private volatile ApplicationContext applicationContext;

    @Value("${scheduler.job.allow-cancel:false}")
    private boolean allowJobCancel;

    /** 重发执行校验，加载任务最前2s内*/
    @Value("${scheduler.job.repeat-check-delta:2000}")
    private long repeatCheckDelta;

    @Resource
    private IJobManager jobManager;

    @Resource
    private IJobSegTriggerFlowManager jobSegTriggerFlowManager;

    @Resource
    private JobSegTriggerExtMapper jobSegTriggerExtMapper;

    @Resource
    private JobShardManager jobShardManager;

    private volatile long currentLoadJobTime;

    /**
     * 调度分片任务
     * @param nextTriggerTime
     * @param jobShardIds
     */
    public void scheduleShardJob(Long nextTriggerTime, Integer maxJobNums, List<Integer> jobShardIds) {
        List<List<Job>> jobs = jobManager.loadRecentlyJobs(jobShardIds, nextTriggerTime, maxJobNums);
        LOG.info("Total jobs-------------------->:{}", jobs.stream().mapToInt(jb->jb.size()).sum());
        if (jobs.size() > 0) {
            LOG.info("Total jobs-------------------->:{}", jobs.stream().mapToInt(jb->jb.size()).sum());
            translateToMap(jobs).entrySet().forEach(entry -> scheduler.schedule(new DelayJob(entry.getValue()), entry.getKey() * 1000 - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 调度一个任务
     * @param job
     */
    public void scheduleOneJob(Job job) {
        if (job.getTriggerTime() <= System.currentTimeMillis()) {
            executor.execute(new InternalDelayJob(job));
        } else {
            scheduler.schedule(new JobProcessor.DelayJob(Arrays.asList(job)), job.getTriggerTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 调度指定批量任务
     * @param jobs
     */
    public void scheduleJobs(List<Job> jobs) {
        translateToMap(Arrays.asList(jobs)).entrySet().forEach(entry -> scheduler.schedule(new DelayJob(entry.getValue()), entry.getKey() * 1000 - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    /**
     * 批量任务转化为按秒升序的TreeMap结构
     * @param jobs
     * @return
     */
    private TreeMap<Long, List<Job>> translateToMap(List<List<Job>> jobs) {
        final TreeMap<Long, List<Job>> mappedJobs = new TreeMap<>();
        jobs.forEach(internalJobs -> internalJobs.forEach(job -> {
            //到期任务直接触发
            if (job.getTriggerTime() <= System.currentTimeMillis()) {
                executor.execute(new InternalDelayJob(job));
            } else {
                // 按秒分组
                long triggerTimeSeconds = job.getTriggerTime() / 1000;
                List<Job> secondJobs = mappedJobs.get(triggerTimeSeconds);
                if (null == secondJobs) {
                    secondJobs = new ArrayList<>();
                    mappedJobs.put(triggerTimeSeconds, secondJobs);
                }
                secondJobs.add(job);
            }
        }));
        return mappedJobs;
    }

    /**
     * 启动触发任务状态同步任务，CACHE->DB
     */
    public void startSyncCompletionJobToDB() {
        for (int idx=0; idx<V_CPU_CORES; idx++) {
            stateExecutor.submit(new CompletionJob());
        }
    }

    /**
     * 补偿失败任务
     */
    public void retryOneJob(Job job) {
        retryExecutor.execute(new RetryJob(job));
    }

    /**
     * 异步同步任务执行状态
     */
    public void triggerFlowStateSync() {
        long minTriggerTime = getMinTriggerTimeFromSegTrigger();
        if (minTriggerTime > 0) {
            int shards = jobShardManager.selectJobShardCount();
            List<JobSegTriggerFlow> flows = jobSegTriggerFlowManager.loadRecentlyFlows(shards, minTriggerTime);
            for (JobSegTriggerFlow flow : flows) {
                int processCount = jobManager.getNoneSuccessJobsCount(flow.getJobShardId(), flow.getTriggerTimeStart(), flow.getTriggerTimeEnd());
                if (processCount == 0) {
                    jobSegTriggerFlowManager.updateTaskFlowState(flow, TriggerFLowState.COMPLETE);
                }
            }
        }
    }

    private long getMinTriggerTimeFromSegTrigger() {
        final JobSegTrigger seg = jobSegTriggerExtMapper.selectOneSegTrigger();
        if (null != seg) {
            return seg.getTriggerTimeStart();
        }
        return -1;
    }

    private void fireJob(Job job) {
        try {
            CallbackProtocol protocol = CallbackProtocol.getByProtocol(job.getCallbackProtocol());
            final IJobTrigger trigger = (IJobTrigger) applicationContext.getBean(protocol.getTrigger());
            TriggerResult resp = trigger.triggerJob(job);
            if (!resp.isSuccessful()) {
                throw new DelayJobException(ExceptionEnum.UNKNOWN_ERROR, resp.getMsg());
            }
            LOG.info("Job[Shard:{}, Job:{}, TriggerTime:{}, CurrentTime:{}] trigger success.", job.getJobShardId(), job.getId(), job.getTriggerTime() / 1000, System.currentTimeMillis() / 1000);
        } catch (Exception ex) {
            LOG.info("Job[Shard:{}, Job:{}, TriggerTime:{}, CurrentTime:{}] trigger error.", job.getJobShardId(), job.getId(), job.getTriggerTime() / 1000, System.currentTimeMillis() / 1000, ex);
        }
    }

    private boolean needSyncJobStateFromDB(Job job) {
        return allowJobCancel && inRepeatTriggerCheckWindow(job);
    }

    private boolean inRepeatTriggerCheckWindow(Job job) {
        return job.getTriggerTime() > getCurrentLoadJobTime() && job.getTriggerTime() <= getCurrentLoadJobTime() + repeatCheckDelta * 1000;
    }

    private boolean isSubmit(InternalDelayJob internal) {
        return JobState.getStateByCode(internal.job.getState()) == JobState.SUBMIT;
    }

    /**
     * 从数据库同步
     * @param wrapperJob
     */
    private void syncJobStateFromDB(InternalDelayJob wrapperJob) {
        Job job = jobManager.loadJob(wrapperJob.job.getOutJobNo());
        if (null != job) {
            wrapperJob.job.setState(job.getState());
        }
    }

    private class InternalDelayJob implements Runnable {

        private final Job job;

        private InternalDelayJob(Job job) {
            this.job = job;
        }

        @Override
        public void run() {
            if (isSubmit(this)) {
                fireJob(job);
            }
        }
    }

    private class RetryJob implements Runnable {

        private final Job job;

        private RetryJob(Job job) {
            this.job = job;
        }

        @Override
        public void run() {
            fireJob(job);
        }
    }

    private class DelayJob implements Runnable {

        private final List<Job> jobs;

        private DelayJob(List<Job> job) {
            this.jobs = job;
        }

        @Override
        public void run() {
            jobs.forEach(job -> executor.execute(new InternalDelayJob(job)));
        }
    }

    private class DelayThreadPoolExecutor extends ThreadPoolExecutor {

        public DelayThreadPoolExecutor(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       ThreadFactory threadFactory,
                                       RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        public DelayThreadPoolExecutor(int threads, ThreadFactory threadFactory) {
            this(threads, threads, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), threadFactory, new AbortPolicy());
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            if (r instanceof InternalDelayJob) {
                InternalDelayJob wrapper = (InternalDelayJob) r;
                if (needSyncJobStateFromDB(wrapper.job)) {
                    syncJobStateFromDB(wrapper);
                }
            }
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (r instanceof InternalDelayJob) {
                InternalDelayJob wrapperJob = (InternalDelayJob) r;
                if (isSubmit(wrapperJob)) {
                    triggeredJobs.offer(new TriggeredJob(wrapperJob.job, null != t ? JobState.RETRY : JobState.SUCCESS));
                }
            } else if (r instanceof RetryJob) {
                RetryJob retry = (RetryJob) r;
                JobState destState = null == t ? JobState.SUCCESS : retry.job.getRetryTimes() > DEFAULT_JOB_RETRY_TIMES ? JobState.FAIL : JobState.RETRY;
                if (jobManager.modifyJobState(retry.job, destState.getCode(), retry.job.getState(), retry.job.getRetryTimes() + 1)) {
                    retry.job.setState(destState.getCode());
                }
            }
        }
    }

    private class TriggeredJob {

        private Job job;

        private JobState destState;

        public TriggeredJob(Job job, JobState destState) {
            this.job = job;
            this.destState = destState;
        }
    }

    private class CompletionJob implements Runnable {

        private final List<Job> success = new ArrayList<>(JOB_STATE_UPDATE_BATCH_SIZE);

        private final List<Job> retry = new ArrayList<>(JOB_STATE_UPDATE_BATCH_SIZE);

        private long costs = 0L;

        @Override
        public void run() {
            try {
                while ((success.size() + retry.size()) < JOB_STATE_UPDATE_BATCH_SIZE && costs < JOB_STATE_UPDATE_TIMEOUT) {
                    long start = System.currentTimeMillis();
                    try {
                        TriggeredJob trigger = triggeredJobs.poll(500, TimeUnit.MICROSECONDS);
                        if (null != trigger) {
                            if (trigger.destState == JobState.RETRY) {
                                retry.add(trigger.job);
                            } else {
                                success.add(trigger.job);
                            }
                        }
                    } catch (InterruptedException ignore) {
                        LOG.warn("Collect triggered job is interrupted.", ignore);
                    }
                    costs = costs + System.currentTimeMillis() - start;
                }
                if (success.size() > 0) {
                    batchModifyJobsState(success, JobState.SUCCESS);
                }
                if (retry.size() > 0) {
                    batchModifyJobsState(retry, JobState.RETRY);
                }
            } finally {
                stateExecutor.submit(new CompletionJob());
            }
        }
    }

    private void batchModifyJobsState(List<Job> jobs, JobState state) {
        Map<Integer, List<Job>> mappedJobs = new HashMap<>();
        jobs.forEach(job -> {
            if (mappedJobs.get(job.getJobShardId()) == null) {
                mappedJobs.put(job.getJobShardId(), new ArrayList<>());
            }
            mappedJobs.get(job.getJobShardId()).add(job);
        });

        mappedJobs.entrySet().forEach(item -> jobManager.modifyJobState(item.getKey(), jobs, state.getCode()));
    }

    public long getCurrentLoadJobTime() {
        return currentLoadJobTime;
    }

    public void setCurrentLoadJobTime(long currentLoadJobTime) {
        this.currentLoadJobTime = currentLoadJobTime;
    }
}