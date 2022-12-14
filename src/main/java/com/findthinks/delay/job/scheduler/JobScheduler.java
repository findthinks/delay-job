package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.id.KeyGeneratorManager;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import com.findthinks.delay.job.share.repository.entity.*;
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
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.V_CPU_CORES;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.*;

@Component
public class JobScheduler {
    
    private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

    private static final String JOB_ID_GENERATE_KEY = "JOB_ID";

    private static final int CLOSE_HANDLER_TIMEOUT = 60;

    private static final int BATCH_JOBS_SIZE = 100;

    private static final int BATCH_THRESHOLD = 4;

    protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(V_CPU_CORES / 2 + 1, new ThreadFactoryBuilder().setNameFormat("Delay-JobShard-%d").setDaemon(true).build());

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
    private IGlobalRecManager globalRecManager;

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    @Value("${scheduler.judge-dead:15}")
    private int judgeDeadInternal;

    @Value("${scheduler.job.retry-max-job-num:1000}")
    private int retryMaxJobNum;

    @Value("${scheduler.job.once-retry-seg-num:8}")
    private int onceRetrySegNum;

    @Value("${scheduler.job.retry-seg-num:32}")
    private int retrySegNum;

    @Value("${scheduler.job.state-check-seg-num:8}")
    private int stateCheckSegNum;

    @Value("${scheduler.job.cron.load:0 0/1 * * * ?}")
    private CronExpression jobLoadCron;

    @Value("${scheduler.job.shard-load-max-job-num:50000}")
    private int shardLoadMaxJobNum;

    @Value("${scheduler.job.translate-max-job-num:100}")
    private int translateMaxJobNum;

    private volatile List<Integer> jobShardIds;

    private volatile long currentScheduleTime = 0;

    private volatile long nextScheduleTime = 0;

    private final Lock retryLocker = new ReentrantLock();

    private final Lock translateLocker = new ReentrantLock();

    /** ???????????? */
    private volatile List<JobShard> assignedJobShard = null;

    /**
     * ??????????????????
     */
    public Long submitJob(FacadeJob facadeJob) {
        //?????????????????????
        Job job = createJob(facadeJob);
        job.setJobShardId(getOneJobShardId());
        jobManager.createJob(job);

        //?????????????????????????????????????????????
        if (shouldScheduleImmediately(job.getTriggerTime())) {
            jobProcessor.scheduleOneJob(job);
        }
        return job.getId();
    }

    /**
     * ????????????????????????
     */
    public void submitJobs(List<FacadeJob> jobs) {
        submitJobs(jobs, getJobShardIds());
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    public void submitJobs(List<FacadeJob> facadeJobs, List<Integer> shardIds) {
        if (facadeJobs.size() > BATCH_JOBS_SIZE) {
            throw new ParamsException("Batch jobs size overflow.");
        }
        int batchSize = facadeJobs.size() / BATCH_THRESHOLD;
        int counter = 0;
        List<GlobalRec> recs = new ArrayList<>(facadeJobs.size());
        List<Job> batchJobs = new ArrayList<>(batchSize);
        List<Job> immediate = new ArrayList<>();
        Map<Integer, List<Job>> jobs = new HashMap<>();
        Integer jobShardId = getOneJobShardId(shardIds);
        for (int idx=0; idx<facadeJobs.size(); idx++) {
            Job job = createJob(facadeJobs.get(idx));
            job.setJobShardId(jobShardId);
            batchJobs.add(job);

            //??????????????????
            recs.add(buildGlobalRec(job.getOutJobNo(), jobShardId, job.getId(), job.getTriggerTime(), new Date()));

            //????????????
            if (counter < batchSize) {
                counter ++;
            } else {
                if (CollectionUtils.isEmpty(jobs.get(jobShardId))) {
                    jobs.put(jobShardId, batchJobs);
                } else {
                    jobs.get(jobShardId).addAll(batchJobs);
                }
                counter = 0;
                jobShardId = getOneJobShardId(shardIds);
                batchJobs = new ArrayList<>(batchSize);
            }

            //?????????????????????????????????????????????
            if (shouldScheduleImmediately(job.getTriggerTime())) {
                immediate.add(job);
            }
        }

        //??????????????????
        if (CollectionUtils.isEmpty(jobs.get(jobShardId))) {
            jobs.put(jobShardId, batchJobs);
        } else {
            jobs.get(jobShardId).addAll(batchJobs);
        }

        //????????????
        jobManager.createJobs(jobs, recs);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        jobProcessor.scheduleJobs(immediate);
    }

    /**
     * ??????????????????
     * @param outJobNo
     */
    public void pauseJob(String outJobNo) {
        jobManager.pause(outJobNo);
    }

    /**
     * ????????????
     * @param outJobNo
     */
    public void resumeJob(String outJobNo) {
        Job resume = jobManager.resume(outJobNo);
        if (shouldScheduleImmediately(resume.getTriggerTime())) {
            jobProcessor.scheduleOneJob(resume);
        }
    }

    /**
     * ??????????????????
     * @param outJobNo
     * @return
     */
    public void cancelJob(String outJobNo) {
        Job job = jobManager.loadJob(outJobNo);
        if (null != job) {
            if (job.getTriggerTime() <= nextScheduleTime) {
                throw new DelayJobException(CANNOT_CANCEL_JOB, "Job is ready to trigger");
            }

            if (JobState.getStateByCode(job.getState()) == JobState.CANCEL) {
                throw new DelayJobException(JOB_IS_CANCEL, "Job is canceled");
            }

            if (job.getState() > JobState.SUBMIT.getCode()) {
                throw new DelayJobException(CANNOT_CANCEL_JOB, "Job is triggered");
            }

            if (!jobManager.modifyJobState(job, JobState.CANCEL.getCode(), JobState.SUBMIT.getCode(), 0)) {
                throw new DelayJobException(CANNOT_CANCEL_JOB, "Job is triggered");
            }
        }
    }

    public void start() {
        /** ??????????????? */
        reRegister(createSchedulerInfo());

        /** ?????????????????? */
        doHeartbeat();

        /** ???????????????????????? */
        doLoadDelayJob();

        /** ???????????????????????? */
        doStateSync();

        /** ???????????????????????? */
        doRetry();

        /** ??????????????? */
        jobProcessor.startTriggerJob();

        /** ???????????????????????????DB */
        jobProcessor.startPersistJob();
    }
    
    /**
     * ????????????????????????????????????????????????
     * @param jobShardId
     */
    public void startJobShard(Integer jobShardId) {
        jobShardManager.updateJobShardState(jobShardId, JobShardState.DISABLED.getCode(), JobShardState.ENABLED.getCode());
    }

    /**
     * ????????????
     * @param jobShardId
     */
    public void stopJobShard(Integer jobShardId) {
        /** ????????????????????????????????????*/
        if (!CollectionUtils.isEmpty(jobShardManager.loadTranslatingJobShards())) {
            throw new DelayJobException(HAS_TRANSLATING_SHARD);
        }

        /** ??????JobShard????????????????????????????????????????????????????????????????????????????????????????????????????????? */
        jobShardManager.updateJobShardState(jobShardId, JobShardState.ENABLED.getCode(), JobShardState.TRANSLATING.getCode());
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

    public void doHeartbeat() {
        LOG.debug("Scheduler[{}] trigger heartbeat.", null == schedulerInfo ? -1 : schedulerInfo.getId());

        if (!isSchedulerReady()) {
            return;
        }

        /** ????????????????????? */
        refreshScheduler();
    }

    public void doStateSync() {
        LOG.debug("Scheduler[{}] trigger sync state.", null == schedulerInfo ? -1 : schedulerInfo.getId());

        if (!isSchedulerReady()) {
            return;
        }

        /** ???????????????????????????????????????????????????????????????????????? */
        List<JobSegTriggerFlow> segments = jobSegTriggerFlowManager.loadUnCompleteSegments(getSegmentStateCheckStartTime(), currentScheduleTime);
        segments.forEach(segment -> {
            Job Job = jobManager.getOneUnSuccessJob(
                    segment.getJobShardId(),
                    segment.getTriggerTimeStart(),
                    segment.getTriggerTimeEnd());
            if (null == Job) {
                jobSegTriggerFlowManager.updateSegmentState(segment, TriggerFLowState.COMPLETE);
            }
        });

        /** ???????????????????????????????????? */
        reSyncJobShards();

        LOG.info("Monitor info: Ready-Queue-Size: {}, Triggered-Queue-Size: {}", jobProcessor.getReadyJobCount(), jobProcessor.getTriggeredJobCount());
    }

    public void doLoadDelayJob() {
        LOG.debug("Scheduler[{}] trigger load delay job.", null == schedulerInfo ? -1 : schedulerInfo.getId());

        if (!isSchedulerReady()) {
            return;
        }

        if (isSchedulerPersistenceExpired()) {
            return;
        }

        /** ??????????????????????????????????????????????????? */
        currentScheduleTime = nextScheduleTime;
        nextScheduleTime = jobLoadCron.getNextValidTimeAfter(new Date()).getTime();

        /** ???????????????????????????????????? */
        jobProcessor.setCurrentLoadJobTime(currentScheduleTime);

        /** ??????????????????????????????ID */
        List<Integer> shardIds = fetchJobShardIds(getAssignedJobShard());
        if (!CollectionUtils.isEmpty(shardIds)) {
            /** ?????????????????? */
            scheduleDelayJob(nextScheduleTime, shardLoadMaxJobNum, shardIds);

            /** ???????????????????????????????????? */
            releaseJobShardReqByOther(shardIds);
        }
    }

    public void doRetry() {
        LOG.debug("Scheduler[{}] trigger retry job.", null == schedulerInfo ? -1 : schedulerInfo.getId());

        boolean ret = false;
        try {
            ret = retryLocker.tryLock(5, TimeUnit.SECONDS);
            if (ret) {
                if (!isSchedulerReady()) {
                    return;
                }

                if (!isLeader(getSchedulerInfo().getId(), schedulerManager.loadAllSchedulerIds())) {
                    return;
                }

                if (currentScheduleTime <= 0 || nextScheduleTime <= 0) {
                    return;
                }

                /** ?????????????????????????????????????????????????????? */
                List<Integer> shardIds = jobShardManager.loadAllJobShards().stream().map(shard -> shard.getId()).collect(Collectors.toList());

                /** ????????????????????????????????? */
                long retryStartTime = getRetryStartTime();
                LOG.info("Current retry segment: [{} - {}].", retryStartTime, currentScheduleTime);
                doSegmentsRetry(jobSegTriggerFlowManager.loadRetrySegments(shardIds, retryStartTime, currentScheduleTime, onceRetrySegNum));
            }
        } catch (InterruptedException ie) {
            LOG.warn("Enter retry lock fail, has interrupted", ie);
        } finally {
            if (ret) {
                retryLocker.unlock();
            }
        }
    }

    /**
     * ??????????????????????????????????????????
     */
    public void doTranslateDisabledShardJobToOther() {
        boolean ret = false;
        try {
            ret = translateLocker.tryLock(5, TimeUnit.SECONDS);
            if (ret) {
                if (!isSchedulerReady()) {
                    return;
                }

                if (!isLeader(getSchedulerInfo().getId(), schedulerManager.loadAllSchedulerIds())) {
                    return;
                }

                /** ???????????????????????????????????? */
                List<Integer> translatingShardIds = fetchJobShardIds(jobShardManager.loadTranslatingJobShards());
                if (CollectionUtils.isEmpty(translatingShardIds)) {
                    return;
                }

                /** ?????????????????????????????????????????????????????? */
                Map<Integer, Long> mappedSegTriggers = jobSegTriggerManager
                        .loadSegTriggers(translatingShardIds)
                        .stream()
                        .collect(Collectors.toMap(JobSegTrigger::getJobShardId, JobSegTrigger::getTriggerTimeEnd));

                /** ???????????????????????? */
                translatingShardIds.forEach(shardId -> translateShardJobToOtherShard(shardId, mappedSegTriggers.get(shardId), translateMaxJobNum));
            }
        } catch (InterruptedException ex) {
            LOG.warn("Enter translate lock fail, has interrupted", ex);
        } finally {
            if (ret) {
                translateLocker.unlock();
            }
        }
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

    public boolean isSchedulerReady() {
        return null != schedulerInfo;
    }

    private void scheduleDelayJob(Long nextScheduleTime, Integer maxJobNums, List<Integer> jobShardIds) {
        LOG.info("Scheduler[id={}] is loading jobs, shards: {}", getSchedulerInfo().getId(), jobShardIds);
        jobProcessor.scheduleShardJob(nextScheduleTime, maxJobNums, jobShardIds);
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
     * ??????????????????????????????????????????????????????
     * @param jobShardId
     */
    private void translateShardJobToOtherShard(Integer jobShardId, Long startTriggerTime, Integer maxNums) {
        /** ???????????????????????????ID */
        List<Integer> shardIds = jobShardManager.loadEnabledJobShards().stream().map(JobShard::getId).collect(Collectors.toList());

        for (;;) {
            List<Job> jobs = jobManager.loadShardJobs(jobShardId, startTriggerTime, maxNums);
            if (CollectionUtils.isEmpty(jobs)) {
                break;
            }
            List<FacadeJob> facadeJobs = jobs.stream().map(job -> buildFacadeJob(job)).collect(Collectors.toList());

            /** ?????????????????? */
            globalRecManager.deleteGlobalRec(jobs.stream().map(Job::getOutJobNo).collect(Collectors.toList()));

            /** ??????????????????????????? */
            submitJobs(facadeJobs, shardIds);

            /** ????????????????????????????????? */
            jobManager.deleteShardJobs(jobShardId, jobs.stream().map(job -> job.getId()).collect(Collectors.toList()));
        }

        /** ???????????????????????????????????????????????? */
        jobShardManager.updateJobShardState(jobShardId, JobShardState.TRANSLATING.getCode(), JobShardState.DISABLED.getCode());
    }

    private void refreshScheduler() {
        /** ??????????????????????????????GC????????????????????????????????????????????????????????? */
        if (isSchedulerPersistenceExpired()) {
            reRegister(createSchedulerInfo());
            return;
        }

        /** ?????????????????? */
        updateSchedulerHeartbeat();

        /** ????????????????????? */
        clearExpiredScheduler();

        /** ???????????????????????????????????????????????? */
        List<Integer> schedulers = schedulerManager.loadAllSchedulerIds();
        if (isLeader(getSchedulerInfo().getId(), schedulers)) {
            LOG.info("Master-scheduler[id={}] is assigning job shards.", getSchedulerInfo().getId());

            /** ????????????????????? */
            releaseJobShardOutOfControl(schedulers);

            /** ?????????????????? */
            reAssignJobShard(schedulers, jobShardManager.loadEnabledJobShards());
        }
    }

    private boolean shouldScheduleImmediately(long planTriggerTime) {
        return planTriggerTime <= System.currentTimeMillis() || (planTriggerTime >= currentScheduleTime && planTriggerTime < nextScheduleTime);
    }

    /**
     * ????????????????????????????????????
     */
    private void reSyncJobShards() {
        final List<Integer> shardIds = jobShardManager.loadEnabledJobShards().stream().map(shard -> shard.getId()).collect(Collectors.toList());
        setJobShardIds(shardIds);
    }

    /** ???????????? */
    private void fillAssignedJobShard() {
        final List<JobShard> jobShards = Collections.synchronizedList(jobShardManager.loadAssignedJobShards(getSchedulerInfo().getId()));
        this.assignedJobShard = jobShards;
    }

    private List<JobShard> getAssignedJobShard() {
        return jobShardManager.loadAssignedJobShards(getSchedulerInfo().getId());
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????
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
            LOG.info("{} schedulers was cleaned.", effect);
        } catch (Exception ignore) {
            //do nothing
        }
    }
    
    private void reAssignJobShard(List<Integer> schedulers, List<JobShard> shards) {
        //????????????????????????map
        Map<Integer, JobShard> mappedShards = shards.stream().collect(Collectors.toMap(JobShard::getId, shard -> shard));

        //??????????????????
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
        //??????scheduler?????????
        getSchedulerInfo().setLastHeartbeatTime(systemTime);
        //??????db????????????
        schedulerManager.updateSchedulerInfoLastHeartbeatTime(systemTime, getSchedulerInfo().getId());
    }

    private Job createJob(FacadeJob facade) {
        Job job = new Job();
        job.setId(generateJobId());
        job.setType(facade.getType());
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
        facadeJob.setType(job.getType());
        facadeJob.setTriggerTime(job.getTriggerTime() / 1000);
        facadeJob.setCallbackProtocol(job.getCallbackEndpoint());
        facadeJob.setCallbackEndpoint(job.getCallbackEndpoint());
        return facadeJob;
    }

    /**
     * ????????????JOB_ID
     * @return
     */
    private Long generateJobId() {
        return keyGeneratorManager.getKeyGenerator(JOB_ID_GENERATE_KEY).nextId();
    }

    /**
     * ?????????????????????????????????????????????
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

    private void releaseJobShardReqByOther(List<Integer> assignedShardIds) {
        jobShardManager.updateReqServerToCurServer(assignedShardIds);
    }

    private void reRegister(SchedulerInfo schedulerInfo) {
        schedulerManager.createSchedulerInfo(schedulerInfo);
        setSchedulerInfo(schedulerInfo);
    }

    private List<Integer> fetchJobShardIds(List<JobShard> jobShard) {
        if (CollectionUtils.isEmpty(jobShard)) {
            return Collections.EMPTY_LIST;
        }
        return jobShard.stream().map(JobShard::getId).collect(Collectors.toList());
    }

    private Integer getOneJobShardId() {
        return getOneJobShardId(getJobShardIds());
    }

    private Integer getOneJobShardId(List<Integer> shardIds) {
        if (CollectionUtils.isEmpty(shardIds)) {
            throw new DelayJobException(NO_AVAILABLE_JOB_SHARD);
        }
        return queueSelector.chooseOne(shardIds);
    }

    /** ???????????? */
    public void doLoadDelayJobInternal() {
        scheduleDelayJob(getNextValidTimeAfter(new Date(nextScheduleTime)), shardLoadMaxJobNum, null);
    }

    private long getNextValidTimeAfter(Date current) {
        return jobLoadCron.getNextValidTimeAfter(current).getTime();
    }

    private long getRetryStartTime() {
        return currentScheduleTime - retrySegNum * (nextScheduleTime - currentScheduleTime);
    }

    private Long getSegmentStateCheckStartTime() {
        return currentScheduleTime - stateCheckSegNum * (nextScheduleTime - currentScheduleTime);
    }

    private void doSegmentsRetry(List<JobSegTriggerFlow> segments) {
        if (!CollectionUtils.isEmpty(segments)) {
            segments.forEach(segment -> {
                List<Job> jobs = jobManager.loadRetryJobs(
                        segment.getJobShardId(),
                        segment.getTriggerTimeStart(),
                        segment.getTriggerTimeEnd(),
                        retryMaxJobNum);
                if (!CollectionUtils.isEmpty(jobs)) {
                    jobs.forEach(job -> {
                        if (!jobProcessor.needDiscardTrigger(job)) {
                            jobProcessor.retryOneJob(job);
                        }});
                } else {
                    jobSegTriggerFlowManager.updateSegmentState(segment, TriggerFLowState.COMPLETE);
                }
            });
        }
    }
}
