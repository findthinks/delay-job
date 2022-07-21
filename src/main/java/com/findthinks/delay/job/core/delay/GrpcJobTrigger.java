package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.Job;
import com.findthinks.delay.job.proto.cb.CallbackResp;
import com.findthinks.delay.job.proto.cb.DelayJobCallbackGrpc;
import com.findthinks.delay.job.proto.cb.JobCallbackReq;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component("grpcJobTrigger")
public class GrpcJobTrigger implements IJobTrigger {

    /** double check*/
    private Object locker = new Object();

    /** 缓存各个回调创建的grpc长链接*/
    private final ConcurrentMap<String, DelayJobCallbackGrpc.DelayJobCallbackBlockingStub> stubs = new ConcurrentHashMap();

    @Override
    public TriggerResult triggerJob(Job job) {
        JobCallbackReq req = JobCallbackReq.newBuilder()
                .setJobInfo(job.getJobInfo())
                .setOutJobNo(job.getOutJobNo())
                .setTriggerTime(job.getTriggerTime())
                .build();
        final CallbackResp resp = getTriggerStub(job.getCallbackEndpoint()).trigger(req);
        return new TriggerResult(resp.getCode(), resp.getMessage());
    }

    private DelayJobCallbackGrpc.DelayJobCallbackBlockingStub getTriggerStub(String endpoint) {
        DelayJobCallbackGrpc.DelayJobCallbackBlockingStub stub = stubs.get(endpoint);
        if (null == stub) {
            synchronized (locker) {
                if (null == (stub = stubs.get(endpoint))) {
                    ManagedChannel channel = ManagedChannelBuilder.forTarget(endpoint).usePlaintext().build();
                    stub = DelayJobCallbackGrpc.newBlockingStub(channel);
                    stubs.put(endpoint, stub);
                }
            }
        }
        return stub;
    }
}