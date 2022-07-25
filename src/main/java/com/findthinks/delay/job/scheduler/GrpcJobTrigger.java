package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component("grpcJobTrigger")
public class GrpcJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcJobTrigger.class);

    /** double check*/
    private Object locker = new Object();

    /** 缓存各个回调创建的grpc长链接*/
    private final ConcurrentMap<String, JobCallbackGrpc.JobCallbackBlockingStub> stubs = new ConcurrentHashMap();

    @Override
    public TriggerResult triggerJob(Job job) {
        CallbackReq req = CallbackReq.newBuilder()
                .setJobInfo(job.getJobInfo())
                .setOutJobNo(job.getOutJobNo())
                .setTriggerTime(job.getTriggerTime())
                .build();
        final CallbackResp resp = getTriggerStub(job.getCallbackEndpoint()).trigger(req);
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