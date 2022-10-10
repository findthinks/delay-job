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
@SuppressWarnings("all")
public class JobProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(JobProcessor.class);

    private static final int SCHEDULER_NUM = V_CPU_CORES;

    private static final int EXECUTOR_NUM = V_CPU_CORES * 4 + 1;

    /** 延时调度器 */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_NUM, new ThreadFactoryBuilder().setNameFormat("Delay-Job-%d").setDaemon(true).build());

    /** 延迟执行处理器 */
    private final ExecutorService executor = Executors.newFixedThreadPool(EXECUTOR_NUM, new ThreadFactoryBuilder().setNameFormat("Job-Executor-%d").setDaemon(true).build());

    /** 异步更新数据库任务状态 */
    private final ExecutorService stateExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Job-State-Sync-%d").setDaemon(true).build());

    /** 收集需立即触发的任务*/
    private final BlockingQueue<Job> readyQueue = new LinkedBlockingQueue<>();

    /** 收集已触发成功任务，批量更新数据库*/
    private final BlockingQueue<Job> triggeredQueue = new LinkedBlockingQueue<>();

    @Value("${scheduler.job.batch-trigger-num: 100}")
    private int batchTriggerNum;

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
             * 故将提交的任务按秒分组缓存后，再提交给DelayQueue排队，提升性能。
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
            stateExecutor.submit(new TriggerJob());
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
        try {
            fireJobs(Arrays.asList(job));

            //修改任务状态为成功
            jobManager.modifyJobState(job, JobState.SUCCESS.getCode(), job.getState(), job.getRetryTimes() + 1);
        } catch (Exception ex) {
            // 状态不变，修改重试次数
            jobManager.modifyJobState(job, job.getState(), job.getState(), job.getRetryTimes() + 1);
        }
    }

    private void fireJobs(List<Job> jobs) {
        Map<String, List<Job>> group = jobs.stream().collect(Collectors.groupingBy(Job::getCallbackEndpoint));
        group.entrySet().stream().forEach(entry -> {
            CallbackProtocol protocol = CallbackProtocol.getByProtocol(entry.getValue().get(0).getCallbackProtocol());
            ((IJobTrigger) applicationContext.getBean(protocol.getTrigger())).triggerJobs(entry.getValue());
        });
    }

    private boolean isSubmit(Job job) {
        return JobState.getStateByCode(job.getState()) == JobState.SUBMIT;
    }

    private void doScheduleJobInternal(Long triggerTime, List<Job> jobs) {
        /** 任务异步触发 */
        jobs.forEach(job -> readyQueue.offer(job));

        /** 任务触发后移除分组缓存 */
        delayingJobs.remove(triggerTime);
    }

    protected boolean supportBatchTriggerJob() {
        return batchTriggerNum > 1;
    }

    private class PersistJob implements Runnable {

        private static final int JOB_STATE_UPDATE_BATCH_SIZE = 500;

        private static final int JOB_STATE_UPDATE_TIMEOUT = 1000;

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
            List<Job> jobs = new ArrayList<>(JOB_STATE_UPDATE_BATCH_SIZE);
            long costs = 0L;
            while (jobs.size() < JOB_STATE_UPDATE_BATCH_SIZE && costs < JOB_STATE_UPDATE_TIMEOUT) {
                long start = System.currentTimeMillis();
                Job job = triggeredQueue.poll(100, TimeUnit.MILLISECONDS);
                if (null != job) {
                    jobs.add(job);
                }
                costs = costs + System.currentTimeMillis() - start;
            }
            if (jobs.size() > 0) {
                modifyJobsState(jobs);
            }
        }

        private void modifyJobsState(List<Job> jobs) {
            Map<Integer, List<Job>> shardJobs = jobs.stream().collect(Collectors.groupingBy(Job::getJobShardId));
            shardJobs.entrySet().forEach(item -> jobManager.modifyJobState(item.getKey(), jobs, JobState.SUCCESS.getCode()));
        }
    }

    private class TriggerJob implements Runnable {

        private static final int JOB_TRIGGER_TIMEOUT = 100;

        @Override
        public void run() {
            try {
                if (supportBatchTriggerJob()) {
                    doTriggerJobs();
                } else {
                    doTriggerJob();
                }
            } catch (InterruptedException ignore) {
                LOG.warn("Get job interrupted.", ignore);
            } finally {
                stateExecutor.submit(new TriggerJob());
            }
        }

        private void doTriggerJob() throws InterruptedException {
            Job job = null;
            while (null != (job = readyQueue.poll(100, TimeUnit.MILLISECONDS))) {
                /** 触发单条任务*/
                doTriggerJobsInternal(Arrays.asList(job));

                /** 收集成功触发任务*/
                triggeredQueue.offer(job);
            }
        }

        private void doTriggerJobs() throws InterruptedException {
            List<Job> jobs = new ArrayList<>(batchTriggerNum);
            long costs = 0L;
            while (jobs.size() < batchTriggerNum && costs < JOB_TRIGGER_TIMEOUT) {
                long start = System.currentTimeMillis();
                Job job = readyQueue.poll(100, TimeUnit.MILLISECONDS);
                if (null != job) {
                    jobs.add(job);
                }
                costs = costs + System.currentTimeMillis() - start;
            }
            if (jobs.size() > 0) {
                doTriggerJobsInternal(jobs);
            }

            /** 收集成功触发任务*/
            jobs.forEach(job -> triggeredQueue.offer(job));
        }

        private void doTriggerJobsInternal(List<Job> jobs) {
            /** Load new state from DB */
            syncJobStateFromDB(jobs);

            /** 触发任务 */
            fireJobs(jobs);
        }
    }

    /**
     * 暂未使用，处理重数据库同步任务的最新状态
     * @param jobs
     */
    protected void syncJobStateFromDB(List<Job> jobs) {
    }

    public void setCurrentLoadJobTime(long currentLoadJobTime) {
        this.currentLoadJobTime = currentLoadJobTime;
    }

    public long getCurrentLoadJobTime() {
        return currentLoadJobTime;
    }
}