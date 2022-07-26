package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.id.KeyGeneratorManager;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.repository.entity.*;
import com.findthinks.delay.job.share.lib.exception.JobCanceledException;
import com.findthinks.delay.job.share.lib.exception.ParamsException;
import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.lib.utils.CronExpression;
import com.findthinks.delay.job.share.lib.utils.UUIDUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.V_CPU_CORES;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.CANNOT_CANCEL_JOB;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.JOB_IS_CANCEL;

@Component
public class JobScheduler {
    
    private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

    private static final String JOB_ID_GENERATE_KEY = "JOB_ID";

    private static final int CLOSE_HANDLER_TIMEOUT = 60;

    private static final int DEFAULT_QUEUE_NUM = 0;

    private static final int BATCH_JOBS_SIZE = 100;

    private static final int BATCH_THRESHOLD = 4;

    protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(V_CPU_CORES / 2 + 1, new ThreadFactoryBuilder().setNameFormat("Delay-JobShard-%d").setDaemon(true).build());

    private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Delay-Job-Translate-%d").setDaemon(true).build());

    private final RoundRobinSelector queueSelector = new RoundRobinSelector();

    private volatile SchedulerInfo schedulerInfo = null;

    @Resource
    private IJobShardManager jobShardManager;

    @Resource
    private ISchedulerManager schedulerManager;

    @Resource
    private IJobManager jobManager;

    @Resource
    private JobProcessor jobProcessor;

    @Resource
    private IJobSegTriggerFlowManager jobSegTriggerFlowManager;

    @Resource
    private IJobSegTriggerManager jobSegTriggerManager;

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    @Value("${scheduler.judge-dead:15}")
    private int judgeDeadInternal;

    @Value("${scheduler.heartbeat-period:5}")
    private long heartBeatPeriod;

    @Value("${scheduler.job.state-sync-period:10}")
    private long stateSyncPeriod;

    @Value("${scheduler.job.retry-period:10}")
    private long retryPeriod;

    @Value("${scheduler.job.retry-max-nums:1000}")
    private int retryMaxJobNums;

    @Value("${scheduler.job.retry-seg-nums:3}")
    private int retrySegNums;

    @Value("${scheduler.job.load-cron}")
    private String loadJobCron;

    @Value("${scheduler.job.load-max-nums:5000}")
    private int loadMaxJobNums;

    @Value("${scheduler.job.translate-max-nums:100}")
    private int translateMaxNums;

    private volatile List<Integer> jobShardIds;

    private volatile long currentScheduleTime = 0;

    private volatile long nextScheduleTime = 0;

    private volatile List<JobShard> assignedJobShard = null;

    /**
     * 创建延迟任务
     */
    public Long submitJob(FacadeJob facadeJob) {
        //创建持久化任务
        Job job = createJob(facadeJob);
        job.setJobShardId(getOneJobShardId());
        jobManager.createJob(job);

        //两次调度间任务直接进入内存调度
        if (shouldSchedulerImmediately(job.getTriggerTime())) {
            jobProcessor.scheduleOneJob(job);
        }
        return job.getId();
    }

    /**
     * 批量创建延迟任务
     */
    public void submitJobs(List<FacadeJob> jobs) {
        if (jobs.size() > BATCH_JOBS_SIZE) {
            throw new ParamsException("Batch jobs size overflow.");
        }
        int batchSize = jobs.size() / BATCH_THRESHOLD;
        int counter = 0;
        List<GlobalRec> recs = new ArrayList<>(jobs.size());
        List<Job> batchJobs = new ArrayList<>(batchSize);
        List<Job> immediate = new ArrayList<>();
        Map<Integer, List<Job>> mappedJobs = new HashMap<>();
        Integer jobShardId = getOneJobShardId();
        for (int idx=0; idx<jobs.size(); idx++) {
            Job job = createJob(jobs.get(idx));
            job.setJobShardId(jobShardId);
            batchJobs.add(job);

            //添加全局记录
            recs.add(buildGlobalRec(job.getOutJobNo(), jobShardId, job.getId(), job.getTriggerTime(), new Date()));

            //批次处理
            if (counter < batchSize) {
                counter ++;
            } else {
                if (CollectionUtils.isEmpty(mappedJobs.get(jobShardId))) {
                    mappedJobs.put(jobShardId, batchJobs);
                } else {
                    mappedJobs.get(jobShardId).addAll(batchJobs);
                }
                counter = 0;
                jobShardId = getOneJobShardId();
                batchJobs = new ArrayList<>(batchSize);
            }

            //两次调度间任务直接进入内存调度
            if (shouldSchedulerImmediately(job.getTriggerTime())) {
                immediate.add(job);
            }
        }

        //放入最后批次
        if (CollectionUtils.isEmpty(mappedJobs.get(jobShardId))) {
            mappedJobs.put(jobShardId, batchJobs);
        } else {
            mappedJobs.get(jobShardId).addAll(batchJobs);
        }

        //任务创建
        jobManager.createJobs(mappedJobs, recs);

        //已经过期任务，立即执行，若此时任务分片被调度到，则可能导致任务重复触发！！
        jobProcessor.scheduleJobs(immediate);
    }

    private GlobalRec buildGlobalRec(String outJobNo, Integer jobShardId, Long jobId, Long triggerTime, Date gmtCreate) {
        GlobalRec rec = new GlobalRec();
        rec.setOutJobNo(outJobNo);
        rec.setJobShardId(jobShardId);
        rec.setJobId(jobId);
        rec.setTriggerTime(triggerTime);
        rec.setGmtCreate(gmtCreate);
        return rec;
    }

    /**
     * 取消延迟任务
     * @param outJobNo
     * @return
     */
    public void cancelJob(String outJobNo) {
        Job job = jobManager.loadJob(outJobNo);
        if (null != job) {
            if (JobState.getStateByCode(job.getState()) == JobState.CANCEL) {
                throw new DelayJobException(JOB_IS_CANCEL, "Job is canceled.");
            }
            if (job.getState() > JobState.SUBMIT.getCode()) {
                throw new DelayJobException(CANNOT_CANCEL_JOB, "Job is triggered.");
            }
            if (!jobManager.modifyJobState(job, JobState.CANCEL.getCode(), JobState.SUBMIT.getCode(), 0)) {
                throw new DelayJobException(CANNOT_CANCEL_JOB, "Job is triggered.");
            }
        }
    }

    public void start() {
        // 注册调度器
        reRegister(createSchedulerInfo());

        // 启动心跳检查
        this.scheduler.schedule(new HeartBeatJob(), 0, TimeUnit.SECONDS);

        // 启动延迟任务扫描
        this.scheduler.schedule(new LoadDelayJob(), 0, TimeUnit.MILLISECONDS);

        // 启动任务状态同步到DB
        this.jobProcessor.startSyncCompletionJobToDB();

        // 启动状态同步任务
        this.scheduler.schedule(new StateJob(), 0, TimeUnit.SECONDS);

        // 注册任务补偿
        this.scheduler.schedule(new RetryJob(), 0, TimeUnit.SECONDS);
    }
    
    public void scheduleDelayJob(Long nextScheduleTime, Integer maxJobNums) {
        LOG.info("Scheduler[id={}] is loading jobs, shards: {}", getSchedulerInfo().getId(), fetchJobShardIds(assignedJobShard));
        if (!CollectionUtils.isEmpty(assignedJobShard)) {
            jobProcessor.scheduleShardJob(nextScheduleTime, maxJobNums, assignedJobShard.stream().map(JobShard::getId).collect(Collectors.toList()));
        }
    }

    public void refreshScheduler() {
        LOG.info("Scheduler[{}] trigger heartbeat.", schedulerInfo.getId());

        //节点存活，由于网络、GC等原因，导致心跳更新不及时，节点被清理
        if (isSchedulerPersistenceExpired()) {
            //重新注册节点信息
            reRegister(createSchedulerInfo());
            return;
        }
        
        //更新心跳信息
        updateSchedulerHeartbeat();
        
        //清理超时调度器
        clearExpiredScheduler();

        //释放游离任务项，并重新分配任务项
        List<Integer> schedulers = schedulerManager.loadAllSchedulerIds();
        if (isLeader(getSchedulerInfo().getId(), schedulers)) {
            LOG.info("Lead-scheduler[id={}] is assigning job shards.", getSchedulerInfo().getId());

            //释放游离任务项
            releaseJobShardOutOfControl(schedulers);

            //重分配任务项
            reAssignJobShard(schedulers, jobShardManager.loadEnabledJobShards());
        }

        //填充当前已经分配的分片信息
        fillSchedulerAssignedJobShard();
    }

    /**
     * 启用分片，分片开始接收和消费任务
     * @param jobShardId
     */
    public void startJobShard(Integer jobShardId) {
        jobShardManager.updateJobShardState(jobShardId, JobShardState.ENABLED.getCode());
    }

    /**
     * 停用分片
     * @param jobShardId
     */
    public void stopJobShard(Integer jobShardId) {
        /** 修改JobShard为数据转移中，此时改分片停止接受任务，且下个调度周期开始将停止任务消费 */
        jobShardManager.updateJobShardState(jobShardId, JobShardState.TRANSLATING.getCode());

        /** 开始迁移当前分片任务到其它分片 */
        executor.execute(() -> translateJobShardToOtherShard(jobShardId));
    }

    /**
     * 抽取分片任务，重新分配到其它分片
     * @param jobShardId
     */
    private void translateJobShardToOtherShard(Integer jobShardId) {
        long startTime = nextScheduleTime;
        /** 每批次处理100条 */
        int maxJobs = translateMaxNums;
        for (;;) {
            List<Job> jobs = jobManager.loadShardJobs(jobShardId, startTime, maxJobs);
            if (CollectionUtils.isEmpty(jobs)) {
                break;
            }
            List<FacadeJob> facadeJobs = jobs.stream().map(job -> buildFacadeJob(job)).collect(Collectors.toList());

            /** 转移任务到其它分片 */
            submitJobs(facadeJobs);

            /** 批量删除转移的分片任务 */
            jobManager.deleteShardJobs(jobShardId, jobs.stream().map(job -> job.getId()).collect(Collectors.toList()));
        }

        /** 完成任务迁移后修改分片状态为停止 */
        jobShardManager.updateJobShardState(jobShardId, JobShardState.DISABLED.getCode());

        /** 删除JobSegTrigger分段信息 */
        jobSegTriggerManager.deleteSegTrigger(jobShardId);
    }

    private boolean shouldSchedulerImmediately(long planTriggerTime) {
        return planTriggerTime < System.currentTimeMillis() || (planTriggerTime >= currentScheduleTime && planTriggerTime <= nextScheduleTime);
    }

    /**
     * 同步持久层任务分片到内存
     */
    private void reSyncJobShards() {
        final List<Integer> shardIds = jobShardManager.loadEnabledJobShards().stream().map(shard -> shard.getId()).collect(Collectors.toList());
        setJobShardIds(shardIds);
    }

    private void fillSchedulerAssignedJobShard() {
        final List<JobShard> jobShards = Collections.synchronizedList(jobShardManager.loadAssignedJobShards(getSchedulerInfo().getId()));
        this.assignedJobShard = jobShards;
    }

    /**
     * 由于某些原因，比如网络抖动，导致心跳信息持久化到数据库，从而出现数据库中的任务调度器信息被过期清除，
     * 此时不再处理内存中的心跳更新，等待调度器管理者对调度器清理。
     */
    private boolean isSchedulerPersistenceExpired() {
        return null == schedulerManager.loadSchedulerInfo(this.getSchedulerInfo().getId());
    }
    
    private void releaseJobShardOutOfControl(List<Integer> schedulerIds) {
        // update job_shard set cur_server = null where cur_server not in (ids);
        jobShardManager.updateJobShardByCondition(schedulerIds);
    }
    
    private boolean isLeader(Integer curScheduler, List<Integer> schedulers) {
        if (schedulers == null || schedulers.size() == 0) {
            return false;
        }
        return curScheduler.equals(schedulers.get(0));
    }
    
    private void clearExpiredScheduler() {
        // delete from scheduler_info where last_heartbeat_time < getSystemTime() - judgeDeadInternal;
        try {
            int effect = schedulerManager.deleteExpiredSchedulerInfo(getSystemTime() - judgeDeadInternal * 1000);
            LOG.debug("{} schedulers was cleaned.", effect);
        } catch (Exception ignore) {
            //do nothing
        }
    }
    
    private void reAssignJobShard(List<Integer> schedulers, List<JobShard> shards) {
        //将分片信息保存为map
        Map<Integer, JobShard> mappedShards = shards.stream().collect(Collectors.toMap(JobShard::getId, shard -> shard));

        //保存分配结果
        int[] shardNums = assignShard(schedulers.size(), shards.size());
        int point = 0;
        int count = 0;
        for (int i=0; i<shards.size(); i++) {
            Integer jobShardId = shards.get(i).getId();
            if (point < schedulers.size() && i >= count + shardNums[point]) {
                count = count + shardNums[point];
                point = point + 1;
            }
            Integer serverId = -1;
            if (point < schedulers.size() ) {
                serverId = schedulers.get(point);
            }
            
            JobShard jobShard = mappedShards.get(jobShardId);
            if (null == jobShard.getCurServer() || jobShard.getCurServer() < 0) {
                jobShardManager.updateJobShardCurServer(jobShard.getId(), serverId);
            } else if (jobShard.getCurServer().equals(serverId)) {
                // do nothing
            } else {
                jobShardManager.updateJobShardReqServer(jobShard.getId(), serverId);
            }
        }
    }

    private int[] assignShard(int serverNum, int shardNum) {
        int[] assignNums = new int[serverNum];
        int numOfSingle = shardNum / serverNum;
        int otherNum = shardNum % serverNum;
        for (int i = 0; i < assignNums.length; i++) {
            if (i < otherNum) {
                assignNums[i] = numOfSingle + 1;
            } else {
                assignNums[i] = numOfSingle;
            }
        }
        return assignNums;
    }

    private static String getLocalIP() {
        String ip = "unknown";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignore) {
            LOG.warn("Get scheduler ip error, the error will ignore.", ignore);
        }
        return ip;
    }

    private void updateSchedulerHeartbeat() {
        long systemTime = getSystemTime();
        //更新scheduler运行时
        getSchedulerInfo().setLastHeartbeatTime(systemTime);
        //更新db心跳时间
        schedulerManager.updateSchedulerInfoLastHeartbeatTime(systemTime, getSchedulerInfo().getId());
    }

    private Job createJob(FacadeJob facade) {
        Job job = new Job();
        job.setId(generateJobId());
        job.setState(JobState.SUBMIT.getCode());
        job.setTriggerTime(facade.getTriggerTime() * 1000);
        job.setOutJobNo(facade.getOutJobNo());
        job.setCallbackProtocol(CallbackProtocol.valueOf(facade.getCallbackProtocol()).getProtocol());
        job.setCallbackEndpoint(facade.getCallbackEndpoint());
        job.setRetryTimes(0);
        job.setGmtCreate(new Date());
        job.setJobInfo(facade.getJobInfo());
        return job;
    }

    private FacadeJob buildFacadeJob(Job job) {
        FacadeJob facadeJob = new FacadeJob();
        facadeJob.setOutJobNo(job.getOutJobNo());
        facadeJob.setJobInfo(job.getJobInfo());
        facadeJob.setTriggerTime(job.getTriggerTime());
        facadeJob.setCallbackProtocol(job.getCallbackEndpoint());
        facadeJob.setCallbackEndpoint(job.getCallbackEndpoint());
        return facadeJob;
    }

    /**
     * 生成一个JOB_ID
     * @return
     */
    private Long generateJobId() {
        return keyGeneratorManager.getKeyGenerator(JOB_ID_GENERATE_KEY).nextId();
    }

    /**
     * 获取系统统一时间，以数据库为准
     * 
     * @return
     */
    private long getSystemTime() {
        return System.currentTimeMillis();
    }

    private SchedulerInfo createSchedulerInfo() {
        SchedulerInfo scheduler = new SchedulerInfo();
        scheduler.setUuid(UUIDUtils.randomUUID());
        scheduler.setRegister(Register.YES.getCode());
        scheduler.setLastHeartbeatTime(getSystemTime());
        scheduler.setIp(getLocalIP());
        return scheduler;
    }

    public void stop() {
        this.scheduler.shutdown();
        try {
            if (!this.scheduler.awaitTermination(CLOSE_HANDLER_TIMEOUT, TimeUnit.SECONDS)) {
                this.scheduler.shutdownNow();
                if (!this.scheduler.awaitTermination(CLOSE_HANDLER_TIMEOUT, TimeUnit.SECONDS)) {
                    LOG.warn("Scheduler[{}] did not terminate!");
                }
            }
        } catch (InterruptedException ignore) {
            LOG.warn("Stop scheduler has been interrupted!");
        }
    }

    private void releaseJobShardReqByOther() {
        if (!CollectionUtils.isEmpty(assignedJobShard)) {
            jobShardManager.updateReqServerToCurServer(assignedJobShard.stream().map(item -> item.getId()).collect(Collectors.toList()));
        }
    }

    private void reRegister(SchedulerInfo schedulerInfo) {
        schedulerManager.createSchedulerInfo(schedulerInfo);
        setSchedulerInfo(schedulerInfo);
    }

    private List<Integer> fetchJobShardIds(List<JobShard> assignedJobShard) {
        if (CollectionUtils.isEmpty(assignedJobShard)) {
            return Collections.EMPTY_LIST;
        }
        return assignedJobShard.stream().map(item -> item.getId()).collect(Collectors.toList());
    }

    private Integer getOneJobShardId() {
        List<Integer> shardIds = getJobShardIds();
        if (CollectionUtils.isEmpty(shardIds)) {
            return DEFAULT_QUEUE_NUM;
        }
        return queueSelector.chooseOne(shardIds);
    }

    public SchedulerInfo getSchedulerInfo() {
        return schedulerInfo;
    }

    public void setSchedulerInfo(SchedulerInfo schedulerInfo) {
        this.schedulerInfo = schedulerInfo;
    }

    public List<Integer> getJobShardIds() {
        return jobShardIds;
    }

    public void setJobShardIds(List<Integer> jobShardIds) {
        this.jobShardIds = jobShardIds;
    }

    private class LoadDelayJob implements Runnable {
        @Override
        public void run() {
            Date current = new Date();
            try {
                if (isSchedulerPersistenceExpired()) {
                    return;
                }

                /** 加载新一轮的任务, 尽可能保证工作节点时间同步，从而让各个节点能同时触发任务加载*/
                //保留上次调度时间
                currentScheduleTime = nextScheduleTime;
                //传递调度起始时间到执行器
                jobProcessor.setCurrentLoadJobTime(currentScheduleTime);

                //计算下次调度时间，并加载延迟任务到内存排队
                CronExpression cron = new CronExpression(loadJobCron);
                nextScheduleTime = cron.getNextValidTimeAfter(current).getTime();
                scheduleDelayJob(nextScheduleTime, loadMaxJobNums);

                /** 释放被其它节点申请的任务 */
                releaseJobShardReqByOther();
            } catch (Throwable thrown) {
                LOG.error(thrown.getMessage(), thrown);
            } finally {
                //尽可能保证工作节点时间同步，让各个节点能同时触发任务调度
                scheduler.schedule(this, nextScheduleTime - current.getTime(), TimeUnit.MILLISECONDS);
            }
        }
    }

    private class HeartBeatJob implements Runnable {
        @Override
        public void run() {
            try {
                refreshScheduler();
            } catch (Throwable thrown) {
                LOG.error(thrown.getMessage(), thrown);
            } finally {
                scheduler.schedule(this, heartBeatPeriod, TimeUnit.SECONDS);
            }
        }
    }

    private class StateJob implements Runnable {
        @Override
        public void run() {
            try {
                List<Integer> schedulers = schedulerManager.loadAllSchedulerIds();
                if (isLeader(getSchedulerInfo().getId(), schedulers)) {
                    //更新数据库任务状态
                    jobProcessor.triggerFlowStateSync();
                }

                //同步任务分片到调度器缓存
                reSyncJobShards();
            } catch (Throwable thrown) {
                LOG.error(thrown.getMessage(), thrown);
            } finally {
                scheduler.schedule(this, stateSyncPeriod, TimeUnit.SECONDS);
            }
        }
    }

    private class RetryJob implements Runnable {
        @Override
        public void run() {
            try {
                List<Integer> schedulers = schedulerManager.loadAllSchedulerIds();
                if (!isLeader(getSchedulerInfo().getId(), schedulers)) {
                    return;
                }
                if (currentScheduleTime <=0 || nextScheduleTime <= 0) {
                    return;
                }

                Long startTime = currentScheduleTime - retrySegNums * (nextScheduleTime - currentScheduleTime);
                List<JobShard> shards = jobShardManager.loadEnabledJobShards();
                if (CollectionUtils.isEmpty(shards)) {
                    return;
                }
                List<Integer> jobShards = shards.stream().map(shard -> shard.getId()).collect(Collectors.toList());
                List<JobSegTriggerFlow> segments = jobSegTriggerFlowManager.loadRetryFlows(jobShards, startTime, currentScheduleTime);
                if (CollectionUtils.isEmpty(segments)) {
                    return;
                }
                segments.forEach(seg -> {
                    final List<Job> jobs = jobManager.loadRetryJobs(seg.getJobShardId(), seg.getTriggerTimeStart(), seg.getTriggerTimeEnd(), retryMaxJobNums);
                    if (!CollectionUtils.isEmpty(jobs)) {
                        jobs.forEach(job -> jobProcessor.retryOneJob(job));
                    } else {
                        jobSegTriggerFlowManager.updateTaskFlowState(seg, TriggerFLowState.COMPLETE);
                    }
                });
            } catch (Throwable thrown) {
                LOG.error(thrown.getMessage(), thrown);
            } finally {
                scheduler.schedule(this, retryPeriod, TimeUnit.SECONDS);
            }
        }
    }
}
