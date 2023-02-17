package com.findthinks.delay.job.facade.grpc;

import com.findthinks.delay.job.scheduler.FacadeJob;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.facade.grpc.mgr.*;
import com.findthinks.delay.job.scheduler.JobType;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.SUCCESS;
import static com.findthinks.delay.job.share.lib.enums.ExceptionEnum.UNKNOWN_ERROR;

@GrpcService
public class DelayJobService extends JobGrpc.JobImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(DelayJobService.class);

    @Resource
    private JobScheduler jobScheduler;

    @Override
    public void submitJob(CreateJobReq req, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.submitJob(FacadeJob.create(
                    req.getOutJobNo(),
                    0 == req.getType() ? JobType.NORMAL.getCode() : req.getType(), // 设置类型默认值为5，即普通延迟任务
                    req.getTriggerTime(),
                    req.getCallbackProtocol(),
                    req.getCallbackEndpoint(),
                    req.getJobInfo()));

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Create delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    @Override
    public void submitJobs(CreateJobsReq request, StreamObserver<JobResp> responseObserver) {
        try {
            final List<FacadeJob> jobs = new ArrayList<>();
            request.getJobsList().forEach(job -> jobs.add(FacadeJob.create(
                    job.getOutJobNo(),
                    0 == job.getType() ? JobType.NORMAL.getCode() : job.getType(), // 设置类型默认值为5，即普通延迟任务
                    job.getTriggerTime(),
                    job.getCallbackProtocol(),
                    job.getCallbackEndpoint(),
                    job.getJobInfo())));
            jobScheduler.submitJobs(jobs);

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Create delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    @Override
    public void cancelJob(OperateJobReq request, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.cancelJob(request.getOutJobNo());

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    @Override
    public void pauseJob(OperateJobReq request, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.pauseJob(request.getOutJobNo());

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    @Override
    public void resumeJob(OperateJobReq request, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.resumeJob(request.getOutJobNo());

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    private void sendSuccessResponse(StreamObserver<JobResp> responseObserver) {
        JobResp resp = JobResp.newBuilder().setCode(SUCCESS.getCode()).setMessage(SUCCESS.getDesc()).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    private void sendFailResponse(Exception ex, StreamObserver<JobResp> responseObserver) {
        JobResp resp = JobResp.newBuilder().setCode(UNKNOWN_ERROR.getCode()).setMessage(UNKNOWN_ERROR.getDesc()).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}