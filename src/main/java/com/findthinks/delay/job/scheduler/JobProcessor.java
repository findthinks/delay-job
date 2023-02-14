package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.entity.Job;
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

    /** 延时调度器 */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_NUM, new ThreadFactoryBuilder().setNameFormat("Delay-Job-%d").setDaemon(true).build());

    /** 延迟执行处理器 */
    private final ExecutorService executor = Executors.newFixedThreadPool(EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Executor-%d").setDaemon(true).build());

    /** 特别延迟执行处理器-如:处理可暂停计时任务 */
    private final ExecutorService specialExecutor = Executors.newFixedThreadPool(EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Special-Executor-%d").setDaemon(true).build());

    /** 异步更新数据库任务状态 */
    private final ExecutorService stateExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Job-State-Sync-%d").setDaemon(true).build());

    /** 收集需立即触发的任务 */
    private final BlockingQueue<Job> readyQueue = new LinkedBlockingQueue<>();

    /** 收集已触发成功任务，批量更新数据库 */
    private final BlockingQueue<Job> triggeredQueue = new LinkedBlockingQueue<>();

    @Value("${scheduler.job.persist.threshold: 500}")
    private int persistThreshold;

    @Value("${scheduler.job.gather.timeout: 100}")
    private int gatherTimeout;

    @Resource
    private volatile ApplicationContext applicationContext;

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
        if (jobs.size() > 0) {
            translateToMap(jobs).entrySet().forEach(entry ->
                scheduler.schedule(() -> doScheduleJobInternal(entry.getKey() * 1000, entry.getValue()),
                        entry.getKey() * 1000 - System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 调度一个任务
     * @param job
     */
    public void scheduleOneJob(Job job) {
        long current = System.currentTimeMillis();
        if (job.getTriggerTime() <= current) {
            readyQueue.add(job);
        } else {
            /**
             * 当前调度周期（当前分总内）内的任务，直接进入内存排队，由于任务量可能很大，会导致DelayQueue大量的排序，
             * 故将提交的任务按秒分组后，再提交给DelayQueue排队，提升性能。
             */
            List<Job> cacheJobs = delayingJobs.get(job.getTriggerTime());
            if (CollectionUtils.isEmpty(cacheJobs)) {
                synchronized (locker) {
                    if (null == delayingJobs.get(job.getTriggerTime())) {
                        final List<Job> jobs = Collections.synchronizedList(new ArrayList<>());
                        jobs.add(job);
                        delayingJobs.put(job.getTriggerTime(), jobs);
                        scheduler.schedule(() -> doScheduleJobInternal(job.getTriggerTime(), jobs), job.getTriggerTime() - current, TimeUnit.MILLISECONDS);
                    }
                }
            } else {
                cacheJobs.add(job);
            }
        }
    }

    /**
     * 调度指定批量任务
     * @param jobs
     */
    public void scheduleJobs(List<Job> jobs) {
        translateToMap(Arrays.asList(jobs)).entrySet().forEach(entry ->
            scheduler.schedule(() -> doScheduleJobInternal(entry.getKey() * 1000, entry.getValue()), entry.getKey() * 1000 - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    /**
     * 待触发任务数
     * @return
     */
    public int getReadyJobCount() {
        return readyQueue.size();
    }

    /**
     * 已触发，待同步数据库状态任务数
     * @return
     */
    public int getTriggeredJobCount() {
        return triggeredQueue.size();
    }

    /**
     * 批量任务转化为按秒升序的SortedMap结构
     * @param jobs
     * @return
     */
    private TreeMap<Long, List<Job>> translateToMap(List<List<Job>> jobs) {
        return jobs.stream().flatMap(List::stream)
                .collect(Collectors.groupingBy(job -> job.getTriggerTime() / 1000, TreeMap::new, Collectors.toList()));
    }

    /**
     * 启动触发线程池任务
     */
    public void startTriggerJob() {
        for (int idx=0; idx<V_CPU_CORES; idx++) {
            executor.submit(new TriggerJob());
        }
    }

    /**
     * 启动触发任务状态同步任务，CACHE->DB
     */
    public void startPersistJob() {
        for (int idx=0; idx<V_CPU_CORES; idx++) {
            stateExecutor.submit(new PersistJob());
        }
    }

    /**
     * 补偿失败任务
     */
    public void retryOneJob(Job job) {
        TriggerResult ret = fireJob(job);
        JobState dest = ret.isSuccessful() ? JobState.SUCCESS : JobState.FAIL;
        jobManager.modifyJobState(job, dest.getCode(), job.getState(), job.getRetryTimes() + 1);
    }

    private TriggerResult fireJob(Job job) {
        IJobTrigger fire = (IJobTrigger) applicationContext.getBean(CallbackProtocol.getByProtocol(job.getCallbackProtocol()).getTrigger());
        TriggerResult result = null;
        try {
            result = fire.trigger(job);
        } catch (Exception ex) {
            LOG.error("Trigger job error.", ex);
            result = TriggerResult.FAIL;
        }

        LOG.info("Job[Shard:{}, Job:{}, TriggerTime:{}, CurrentTime:{}] trigger {}.", job.getJobShardId(), job.getId(), job.getTriggerTime(), System.currentTimeMillis() / 1000, result.isSuccessful() ? "success" : "fail");
        return result;
    }

    private void doScheduleJobInternal(Long triggerTime, List<Job> jobs) {
        try {
            /** 任务异步触发 */
            jobs.forEach(job -> readyQueue.offer(job));
        } finally {
            /** 任务触发后移除分组缓存 */
            delayingJobs.remove(triggerTime);
        }
    }

    private class PersistJob implements Runnable {

        private static final int WAIT_ONE_JOB_TIMEOUT = 100;

        @Override
        public void run() {
            try {
                doPersistState();
            } catch (InterruptedException ignore) {
                LOG.warn("Gather triggered job interrupted.", ignore);
            } finally {
                stateExecutor.submit(new PersistJob());
            }
        }

        private void doPersistState() throws InterruptedException {
            List<Job> success = new ArrayList<>(persistThreshold);
            long costs = 0L;
            while (success.size() < persistThreshold && costs < gatherTimeout) {
                long start = System.currentTimeMillis();

                Job job = triggeredQueue.poll(WAIT_ONE_JOB_TIMEOUT, TimeUnit.MILLISECONDS);
                if (null == job) {
                    continue;
                }

                success.add(job);

                costs = costs + System.currentTimeMillis() - start;
            }

            /** 批量持久化成功、失败任务状态 */
            modifyJobsState(success, JobState.SUCCESS);
        }

        private void modifyJobsState(List<Job> jobs, JobState destState) {
            if (!CollectionUtils.isEmpty(jobs)) {
                Map<Integer, List<Job>> shardJobs = jobs.stream().collect(Collectors.groupingBy(Job::getJobShardId));
                shardJobs.entrySet().forEach(item -> jobManager.modifyJobState(item.getKey(), jobs, destState.getCode()));
            }
        }
    }

    private class TriggerJob implements Runnable {

        private static final int JOB_TRIGGER_TIMEOUT = 1000;

        @Override
        public void run() {
            try {
                doTriggerJob();
            } catch (Exception ex) {
                LOG.warn("Get job error.", ex);
            } finally {
                stateExecutor.submit(new TriggerJob());
            }
        }

        private void doTriggerJob() throws InterruptedException {
            for (;;) {
                final Job job = readyQueue.poll(JOB_TRIGGER_TIMEOUT, TimeUnit.MILLISECONDS);

                if (null == job) {
                    continue;
                }

                if (needTriggerBySpecialExecutor(job)) {
                    specialExecutor.execute(() -> doTriggerJobInternal(job));
                    continue;
                }

                doTriggerJobInternal(job);
            }
        }
    }

    private void doTriggerJobInternal(Job job) {
        /** 从db同步最新状态 */
        syncJobStateFromDB(job);

        /** 判断是否需要丢弃当次触发 */
        if (needDiscardTrigger(job)) {
            return;
        }

        /** 执行任务触发，将成功的任务放入触发完成队列 */
        if (fireJob(job).isSuccessful()) {
            triggeredQueue.offer(job);
        }
    }

    protected boolean needTriggerBySpecialExecutor(Job job) {
        /** 可暂停任务使用特殊触发器处理 */
        return JobType.getTypeByCode(job.getType()) == JobType.PAUSABLE;
    }

    protected boolean needDiscardTrigger(Job job) {
        /** 暂停中的任务，丢弃本次触发 */
        return null != job.getPauseTime();
    }

    /**
     * 暂未使用，处理重数据库同步任务的最新状态
     * @param job
     */
    protected void syncJobStateFromDB(Job job) {
        if (job.getType().equals(JobType.PAUSABLE.getCode())) {
            Job persist = jobManager.loadJob(job.getJobShardId(), job.getId());
            job.setState(persist.getState());
            job.setTriggerTime(persist.getTriggerTime());
            job.setPauseTime(persist.getPauseTime());
            job.setRetryTimes(persist.getRetryTimes());
        }
    }

    public void setCurrentLoadJobTime(long currentLoadJobTime) {
        this.currentLoadJobTime = currentLoadJobTime;
    }

    public long getCurrentLoadJobTime() {
        return currentLoadJobTime;
    }
}