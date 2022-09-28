package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.entity.Job;
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
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.V_CPU_CORES;

@Component
public class JobProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(JobProcessor.class);

    private static final int SCHEDULER_NUM = V_CPU_CORES;

    private static final int EXECUTOR_NUM = V_CPU_CORES * 4 + 1;

    private static final int DEFAULT_JOB_RETRY_TIMES = 3;

    private static final int JOB_STATE_UPDATE_BATCH_SIZE = 50;

    private static final int JOB_STATE_UPDATE_TIMEOUT = 1000;

    private static final int RETRY_EXECUTOR_NUM = 1;

    /** 延时调度器 */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_NUM, new ThreadFactoryBuilder().setNameFormat("Delay-Job-%d").setDaemon(true).build());

    /** 延迟执行处理器 */
    private final ExecutorService executor = new DelayThreadPoolExecutor(EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Executor-%d").setDaemon(true).build());

    /** 补偿处理器 */
    private final DelayThreadPoolExecutor retryExecutor = new DelayThreadPoolExecutor(RETRY_EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Retry-%d").setDaemon(true).build());

    /** 异步更新数据库任务状态 */
    private final ExecutorService stateExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Job-State-Sync-%d").setDaemon(true).build());

    /** 收集已触发成功任务，批量更新数据库*/
    private final BlockingQueue<TriggeredJob> triggeredJobs = new LinkedBlockingQueue<>();

    @Resource
    private volatile ApplicationContext applicationContext;

    /** 重发执行校验，加载任务最前2s内*/
    @Value("${scheduler.job.repeat-check-delta:2000}")
    private long repeatCheckDelta;

    @Resource
    private IJobManager jobManager;

    private volatile long currentLoadJobTime;

    private final Object locker = new Object();

    private volatile Map<Long, List<Job>> delayingJobs = Collections.synchronizedSortedMap(new TreeMap<>());

    /**
     * 调度分片任务
     * @param nextTriggerTime
     * @param jobShardIds
     */
    public void scheduleShardJob(Long nextTriggerTime, Integer maxJobNums, List<Integer> jobShardIds) {
        List<List<Job>> jobs = jobManager.loadRecentlyJobs(jobShardIds, nextTriggerTime, maxJobNums);
        LOG.info("Load jobs from DB, count: {}.", jobs.stream().mapToInt(List::size).sum());

        if (jobs.size() > 0) {
            translateToMap(jobs).entrySet().forEach(entry ->
                scheduler.schedule(
                        new DelayJob(entry.getKey() * 1000, entry.getValue()),
                        entry.getKey() * 1000 - System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS));
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
            /**
             * 当前调度周期（当前分总内）内的任务，直接进入内存排队，由于任务量可能很大，会导致DelayQueue大量的排序，
             * 故将提交的任务按秒分组缓存后，再提交给DelayQueue排队，提升性能。
             */
            List<Job> jobs = delayingJobs.get(job.getTriggerTime());
            if (CollectionUtils.isEmpty(jobs)) {
                synchronized (locker) {
                    if (null == delayingJobs.get(job.getTriggerTime())) {
                        jobs = Collections.synchronizedList(new ArrayList<>());
                        jobs.add(job);
                        delayingJobs.put(job.getTriggerTime(), jobs);
                        scheduler.schedule(new DelayJob(job.getTriggerTime(), jobs), job.getTriggerTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    }
                }
            } else {
                jobs.add(job);
            }
        }
    }

    /**
     * 调度指定批量任务
     * @param jobs
     */
    public void scheduleJobs(List<Job> jobs) {
        translateToMap(Arrays.asList(jobs)).entrySet().forEach(entry ->
            scheduler.schedule(new DelayJob(entry.getKey() * 1000, entry.getValue()), entry.getKey() * 1000 - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    /**
     * 批量任务转化为按秒升序的SortedMap结构
     * @param jobs
     * @return
     */
    private TreeMap<Long, List<Job>> translateToMap(List<List<Job>> jobs) {
        final TreeMap<Long, List<Job>> mappedJobs = new TreeMap<>();
        jobs.stream().flatMap(List::stream).forEach(job -> {
            long triggerTimeSeconds = job.getTriggerTime() / 1000;
            List<Job> secondsJobs = mappedJobs.get(triggerTimeSeconds);
            if (null == secondsJobs) {
                secondsJobs = new ArrayList<>();
                mappedJobs.put(triggerTimeSeconds, secondsJobs);
            }
            secondsJobs.add(job);
        });
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
     * 获取正在重试的任务数量
     * @return
     */
    public long getRetryingJobCount() {
        return retryExecutor.getQueue().size();
    }

    private void fireJob(Job job) {
        CallbackProtocol protocol = CallbackProtocol.getByProtocol(job.getCallbackProtocol());
        final IJobTrigger trigger = (IJobTrigger) applicationContext.getBean(protocol.getTrigger());
        TriggerResult resp = trigger.triggerJob(job);
        if (!resp.isSuccessful()) {
            throw new DelayJobException(ExceptionEnum.UNKNOWN_ERROR, resp.getMsg());
        }
    }

    private boolean needSyncJobStateFromDB(Job job) {
        return false;
        // return inRepeatTriggerCheckWindow(job);
    }

    /**
     * 暂时不用
     * @param job
     * @return
     */
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
                try {
                    fireJob(job);
                } catch (Exception ex) {
                    LOG.info("Job[Shard:{}, Job:{}, TriggerTime:{}, CurrentTime:{}] trigger error.", job.getJobShardId(), job.getId(), job.getTriggerTime() / 1000, System.currentTimeMillis() / 1000, ex);
                }
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

    class DelayJob implements Runnable {

        private final Long triggerTime;

        private final List<Job> jobs;

        private DelayJob(Long triggerTime, List<Job> jobs) {
            this.jobs = jobs;
            this.triggerTime = triggerTime;
        }

        @Override
        public void run() {
            jobs.forEach(job -> executor.execute(new InternalDelayJob(job)));

            /**
             * 任务触发后移除分组缓存
             */
            delayingJobs.remove(triggerTime);
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
                /** 任务重试成功 */
                if (null == t) {
                    triggeredJobs.offer(new TriggeredJob(retry.job, JobState.SUCCESS));
                /** 任务重试失败或者继续重试 */
                } else {
                    JobState destState = retry.job.getRetryTimes() > DEFAULT_JOB_RETRY_TIMES ? JobState.FAIL : JobState.RETRY;
                    if (jobManager.modifyJobState(
                            retry.job,
                            destState.getCode(),
                            retry.job.getState(),
                            retry.job.getRetryTimes() + 1)) {
                        retry.job.setState(destState.getCode());
                    }
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
                        TriggeredJob trigger = triggeredJobs.poll(500, TimeUnit.MILLISECONDS);
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