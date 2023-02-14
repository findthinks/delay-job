package com.findthinks.delay.job.facade.grpc.cb;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.2)",
    comments = "Source: JobCallback.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class JobCallbackGrpc {

  private JobCallbackGrpc() {}

  public static final String SERVICE_NAME = "delay.job.JobCallback";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.cb.CallbackReq,
      com.findthinks.delay.job.facade.grpc.cb.CallbackResp> getTriggerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "trigger",
      requestType = com.findthinks.delay.job.facade.grpc.cb.CallbackReq.class,
      responseType = com.findthinks.delay.job.facade.grpc.cb.CallbackResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.cb.CallbackReq,
      com.findthinks.delay.job.facade.grpc.cb.CallbackResp> getTriggerMethod() {
    io.grpc.MethodDescriptor<com.findthinks.delay.job.facade.grpc.cb.CallbackReq, com.findthinks.delay.job.facade.grpc.cb.CallbackResp> getTriggerMethod;
    if ((getTriggerMethod = JobCallbackGrpc.getTriggerMethod) == null) {
      synchronized (JobCallbackGrpc.class) {
        if ((getTriggerMethod = JobCallbackGrpc.getTriggerMethod) == null) {
          JobCallbackGrpc.getTriggerMethod = getTriggerMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.facade.grpc.cb.CallbackReq, com.findthinks.delay.job.facade.grpc.cb.CallbackResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "trigger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.cb.CallbackReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.facade.grpc.cb.CallbackResp.getDefaultInstance()))
              .setSchemaDescriptor(new JobCallbackMethodDescriptorSupplier("trigger"))
              .build();
        }
      }
    }
    return getTriggerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static JobCallbackStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobCallbackStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobCallbackStub>() {
        @java.lang.Override
        public JobCallbackStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobCallbackStub(channel, callOptions);
        }
      };
    return JobCallbackStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static JobCallbackBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobCallbackBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobCallbackBlockingStub>() {
        @java.lang.Override
        public JobCallbackBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobCallbackBlockingStub(channel, callOptions);
        }
      };
    return JobCallbackBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static JobCallbackFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<JobCallbackFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<JobCallbackFutureStub>() {
        @java.lang.Override
        public JobCallbackFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new JobCallbackFutureStub(channel, callOptions);
        }
      };
    return JobCallbackFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class JobCallbackImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 任务回调
     * </pre>
     */
    public void trigger(com.findthinks.delay.job.facade.grpc.cb.CallbackReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.cb.CallbackResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTriggerMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getTriggerMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.findthinks.delay.job.facade.grpc.cb.CallbackReq,
                com.findthinks.delay.job.facade.grpc.cb.CallbackResp>(
                  this, METHODID_TRIGGER)))
          .build();
    }
  }

  /**
   */
  public static final class JobCallbackStub extends io.grpc.stub.AbstractAsyncStub<JobCallbackStub> {
    private JobCallbackStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobCallbackStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobCallbackStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务回调
     * </pre>
     */
    public void trigger(com.findthinks.delay.job.facade.grpc.cb.CallbackReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.cb.CallbackResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTriggerMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class JobCallbackBlockingStub extends io.grpc.stub.AbstractBlockingStub<JobCallbackBlockingStub> {
    private JobCallbackBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobCallbackBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobCallbackBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务回调
     * </pre>
     */
    public com.findthinks.delay.job.facade.grpc.cb.CallbackResp trigger(com.findthinks.delay.job.facade.grpc.cb.CallbackReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTriggerMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class JobCallbackFutureStub extends io.grpc.stub.AbstractFutureStub<JobCallbackFutureStub> {
    private JobCallbackFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JobCallbackFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new JobCallbackFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务回调
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.findthinks.delay.job.facade.grpc.cb.CallbackResp> trigger(
        com.findthinks.delay.job.facade.grpc.cb.CallbackReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTriggerMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_TRIGGER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final JobCallbackImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(JobCallbackImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TRIGGER:
          serviceImpl.trigger((com.findthinks.delay.job.facade.grpc.cb.CallbackReq) request,
              (io.grpc.stub.StreamObserver<com.findthinks.delay.job.facade.grpc.cb.CallbackResp>) responseObserver);
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

  private static abstract class JobCallbackBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    JobCallbackBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.findthinks.delay.job.facade.grpc.cb.JobCallbackOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("JobCallback");
    }
  }

  private static final class JobCallbackFileDescriptorSupplier
      extends JobCallbackBaseDescriptorSupplier {
    JobCallbackFileDescriptorSupplier() {}
  }

  private static final class JobCallbackMethodDescriptorSupplier
      extends JobCallbackBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    JobCallbackMethodDescriptorSupplier(String methodName) {
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
      synchronized (JobCallbackGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new JobCallbackFileDescriptorSupplier())
              .addMethod(getTriggerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
