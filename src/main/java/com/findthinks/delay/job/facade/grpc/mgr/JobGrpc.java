package com.findthinks.delay.job.facade.grpc.mgr;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.2)",
    comments = "Source: Job.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class JobGrpc {

  private JobGrpc() {}

  public static final String SERVICE_NAME = "delay.job.Job";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "submitJob",
      requestType = com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq.class,
      responseType = com.findthinks.delay.job.facade.grpc.mgr.JobResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobMethod() {
    io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobMethod;
    if ((getSubmitJobMethod = JobGrpc.getSubmitJobMethod) == null) {
      synchronized (JobGrpc.class) {
        if ((getSubmitJobMethod = JobGrpc.getSubmitJobMethod) == null) {
          JobGrpc.getSubmitJobMethod = getSubmitJobMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new JobMethodDescriptorSupplier("submitJob"))
              .build();
        }
      }
    }
    return getSubmitJobMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "submitJobs",
      requestType = com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq.class,
      responseType = com.findthinks.delay.job.facade.grpc.mgr.JobResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobsMethod() {
    io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp> getSubmitJobsMethod;
    if ((getSubmitJobsMethod = JobGrpc.getSubmitJobsMethod) == null) {
      synchronized (JobGrpc.class) {
        if ((getSubmitJobsMethod = JobGrpc.getSubmitJobsMethod) == null) {
          JobGrpc.getSubmitJobsMethod = getSubmitJobsMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitJobs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new JobMethodDescriptorSupplier("submitJobs"))
              .build();
        }
      }
    }
    return getSubmitJobsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getCancelJobMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cancelJob",
      requestType = com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq.class,
      responseType = com.findthinks.delay.job.facade.grpc.mgr.JobResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq,
      com.findthinks.delay.job.facade.grpc.mgr.JobResp> getCancelJobMethod() {
    io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp> getCancelJobMethod;
    if ((getCancelJobMethod = JobGrpc.getCancelJobMethod) == null) {
      synchronized (JobGrpc.class) {
        if ((getCancelJobMethod = JobGrpc.getCancelJobMethod) == null) {
          JobGrpc.getCancelJobMethod = getCancelJobMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cancelJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new JobMethodDescriptorSupplier("cancelJob"))
              .build();
        }
      }
    }
    return getCancelJobMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static JobStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobStub>() {
        @java.lang.Override
        public JobStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobStub(channel, callOptions);
        }
      };
    return JobStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static JobBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobBlockingStub>() {
        @java.lang.Override
        public JobBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobBlockingStub(channel, callOptions);
        }
      };
    return JobBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static JobFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobFutureStub>() {
        @java.lang.Override
        public JobFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobFutureStub(channel, callOptions);
        }
      };
    return JobFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class JobImplBase implements io.grpc.BindableService {

    /**
     */
    public void submitJob(com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitJobMethod(), responseObserver);
    }

    /**
     */
    public void submitJobs(com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitJobsMethod(), responseObserver);
    }

    /**
     */
    public void cancelJob(com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelJobMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubmitJobMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq,
                com.findthinks.delay.job.facade.grpc.mgr.JobResp>(
                  this, METHODID_SUBMIT_JOB)))
          .addMethod(
            getSubmitJobsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq,
                com.findthinks.delay.job.facade.grpc.mgr.JobResp>(
                  this, METHODID_SUBMIT_JOBS)))
          .addMethod(
            getCancelJobMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq,
                com.findthinks.delay.job.facade.grpc.mgr.JobResp>(
                  this, METHODID_CANCEL_JOB)))
          .build();
    }
  }

  /**
   */
  public static final class JobStub extends io.grpc.stub.AbstractAsyncStub<JobStub> {
    private JobStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobStub(channel, callOptions);
    }

    /**
     */
    public void submitJob(com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitJobMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void submitJobs(com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitJobsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelJob(com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelJobMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class JobBlockingStub extends io.grpc.stub.AbstractBlockingStub<JobBlockingStub> {
    private JobBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.findthinks.delay.job.facade.grpc.mgr.JobResp submitJob(com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitJobMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.findthinks.delay.job.facade.grpc.mgr.JobResp submitJobs(com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitJobsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.findthinks.delay.job.facade.grpc.mgr.JobResp cancelJob(com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelJobMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class JobFutureStub extends io.grpc.stub.AbstractFutureStub<JobFutureStub> {
    private JobFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.findthinks.delay.job.facade.grpc.mgr.JobResp> submitJob(
        com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitJobMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.findthinks.delay.job.facade.grpc.mgr.JobResp> submitJobs(
        com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitJobsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.findthinks.delay.job.facade.grpc.mgr.JobResp> cancelJob(
        com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelJobMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SUBMIT_JOB = 0;
  private static final int METHODID_SUBMIT_JOBS = 1;
  private static final int METHODID_CANCEL_JOB = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final JobImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(JobImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_JOB:
          serviceImpl.submitJob((com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq) request,
              (io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp>) responseObserver);
          break;
        case METHODID_SUBMIT_JOBS:
          serviceImpl.submitJobs((com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq) request,
              (io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp>) responseObserver);
          break;
        case METHODID_CANCEL_JOB:
          serviceImpl.cancelJob((com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq) request,
              (io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.mgr.JobResp>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class JobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    JobBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.findthinks.delay.job.facade.grpc.mgr.JobOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Job");
    }
  }

  private static final class JobFileDescriptorSupplier
      extends JobBaseDescriptorSupplier {
    JobFileDescriptorSupplier() {}
  }

  private static final class JobMethodDescriptorSupplier
      extends JobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    JobMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (JobGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new JobFileDescriptorSupplier())
              .addMethod(getSubmitJobMethod())
              .addMethod(getSubmitJobsMethod())
              .addMethod(getCancelJobMethod())
              .build();
        }
      }
    }
    return result;
  }
}
