package com.findthinks.delay.job.facade.grpc.mgr;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.2)",
    comments = "Source: DelayJob.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DelayJobGrpc {

  private DelayJobGrpc() {}

  public static final String SERVICE_NAME = "delay.job.DelayJob";

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
    if ((getSubmitJobMethod = DelayJobGrpc.getSubmitJobMethod) == null) {
      synchronized (DelayJobGrpc.class) {
        if ((getSubmitJobMethod = DelayJobGrpc.getSubmitJobMethod) == null) {
          DelayJobGrpc.getSubmitJobMethod = getSubmitJobMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CreateJobReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new DelayJobMethodDescriptorSupplier("submitJob"))
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
    if ((getSubmitJobsMethod = DelayJobGrpc.getSubmitJobsMethod) == null) {
      synchronized (DelayJobGrpc.class) {
        if ((getSubmitJobsMethod = DelayJobGrpc.getSubmitJobsMethod) == null) {
          DelayJobGrpc.getSubmitJobsMethod = getSubmitJobsMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitJobs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CreateJobsReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new DelayJobMethodDescriptorSupplier("submitJobs"))
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
    if ((getCancelJobMethod = DelayJobGrpc.getCancelJobMethod) == null) {
      synchronized (DelayJobGrpc.class) {
        if ((getCancelJobMethod = DelayJobGrpc.getCancelJobMethod) == null) {
          DelayJobGrpc.getCancelJobMethod = getCancelJobMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq, com.findthinks.delay.job.facade.grpc.mgr.JobResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cancelJob"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.CancelJobReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.mgr.JobResp.getDefaultInstance()))
              .setSchemaDescriptor(new DelayJobMethodDescriptorSupplier("cancelJob"))
              .build();
        }
      }
    }
    return getCancelJobMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DelayJobStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobStub>() {
        @java.lang.Override
        public DelayJobStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobStub(channel, callOptions);
        }
      };
    return DelayJobStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DelayJobBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobBlockingStub>() {
        @java.lang.Override
        public DelayJobBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobBlockingStub(channel, callOptions);
        }
      };
    return DelayJobBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DelayJobFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobFutureStub>() {
        @java.lang.Override
        public DelayJobFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobFutureStub(channel, callOptions);
        }
      };
    return DelayJobFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class DelayJobImplBase implements io.grpc.BindableService {

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
  public static final class DelayJobStub extends io.grpc.stub.AbstractAsyncStub<DelayJobStub> {
    private DelayJobStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobStub(channel, callOptions);
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
  public static final class DelayJobBlockingStub extends io.grpc.stub.AbstractBlockingStub<DelayJobBlockingStub> {
    private DelayJobBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobBlockingStub(channel, callOptions);
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
  public static final class DelayJobFutureStub extends io.grpc.stub.AbstractFutureStub<DelayJobFutureStub> {
    private DelayJobFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobFutureStub(channel, callOptions);
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
    private final DelayJobImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DelayJobImplBase serviceImpl, int methodId) {
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

  private static abstract class DelayJobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DelayJobBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.findthinks.delay.job.facade.grpc.mgr.DelayJobOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DelayJob");
    }
  }

  private static final class DelayJobFileDescriptorSupplier
      extends DelayJobBaseDescriptorSupplier {
    DelayJobFileDescriptorSupplier() {}
  }

  private static final class DelayJobMethodDescriptorSupplier
      extends DelayJobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DelayJobMethodDescriptorSupplier(String methodName) {
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
      synchronized (DelayJobGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DelayJobFileDescriptorSupplier())
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
