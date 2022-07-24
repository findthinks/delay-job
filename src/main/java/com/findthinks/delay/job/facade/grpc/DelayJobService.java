package com.findthinks.delay.job.facade.grpc;

import com.findthinks.delay.job.core.delay.FacadeJob;
import com.findthinks.delay.job.core.delay.JobScheduler;
import com.findthinks.delay.job.facade.grpc.mgr.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static com.findthinks.delay.job.share.constants.SystemConstants.*;

@GrpcService
public class DelayJobService extends DelayJobGrpc.DelayJobImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(DelayJobService.class);

    @Resource
    private JobScheduler jobScheduler;

    @Override
    public void submitJob(CreateJobReq req, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.submitJob(FacadeJob.create(
                    req.getOutJobNo(),
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
    public void cancelJob(CancelJobReq request, StreamObserver<JobResp> responseObserver) {
        try {
            jobScheduler.cancelJob(request.getOutJobNo());

            sendSuccessResponse(responseObserver);
        } catch (Exception ex) {
            LOG.error("Cancel delay job error.", ex);
            sendFailResponse(ex, responseObserver);
        }
    }

    private void sendSuccessResponse(StreamObserver<JobResp> responseObserver) {
        JobResp resp = JobResp.newBuilder().setCode(JOB_SUCCESS).setMessage(JOB_SUCCESS_MSG).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    private void sendFailResponse(Exception ex, StreamObserver<JobResp> responseObserver) {
        JobResp resp = JobResp.newBuilder().setCode(JOB_FAIL).setMessage(JOB_FAIL_MSG).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
