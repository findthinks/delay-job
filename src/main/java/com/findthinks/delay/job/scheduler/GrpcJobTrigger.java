package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.facade.grpc.cb.CallbackJobInfo;
import com.findthinks.delay.job.facade.grpc.cb.CallbackReq;
import com.findthinks.delay.job.facade.grpc.cb.CallbackResp;
import com.findthinks.delay.job.facade.grpc.cb.JobCallbackGrpc;
import com.findthinks.delay.job.share.repository.entity.Job;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component("grpcJobTrigger")
public class GrpcJobTrigger implements IJobTrigger {

    /** double check*/
    private Object locker = new Object();

    /** 缓存各个回调创建的grpc长链接*/
    private final ConcurrentMap<String, JobCallbackGrpc.JobCallbackBlockingStub> stubs = new ConcurrentHashMap();

    @Override
    public TriggerResult triggerJobs(List<Job> jobs) {
        CallbackReq.Builder builder = CallbackReq.newBuilder();
        jobs.forEach(job -> builder.addJobs(CallbackJobInfo.newBuilder()
                .setOutJobNo(job.getOutJobNo())
                .setTriggerTime(job.getTriggerTime())
                .setJobInfo(job.getJobInfo()).build()));
        CallbackReq req = builder.build();
        final CallbackResp resp = getTriggerStub(jobs.get(0).getCallbackEndpoint()).trigger(req);
        return new TriggerResult(resp.getCode(), resp.getMessage());
    }

    private JobCallbackGrpc.JobCallbackBlockingStub getTriggerStub(String endpoint) {
        JobCallbackGrpc.JobCallbackBlockingStub stub = stubs.get(endpoint);
        if (null == stub) {
            synchronized (locker) {
                if (null == (stub = stubs.get(endpoint))) {
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(endpoint).usePlaintext().build();
                    stub = JobCallbackGrpc.newBlockingStub(channel);
                    stubs.put(endpoint, stub);
                }
            }
        }
        return stub;
    }
}