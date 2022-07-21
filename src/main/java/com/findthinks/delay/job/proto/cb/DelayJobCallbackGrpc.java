package com.findthinks.delay.job.proto.cb;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.2)",
    comments = "Source: DelayJobCallback.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DelayJobCallbackGrpc {

  private DelayJobCallbackGrpc() {}

  public static final String SERVICE_NAME = "delay.job.DelayJobCallback";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.findthinks.delay.job.proto.cb.JobCallbackReq,
      com.findthinks.delay.job.proto.cb.CallbackResp> getTriggerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "trigger",
      requestType = com.findthinks.delay.job.proto.cb.JobCallbackReq.class,
      responseType = com.findthinks.delay.job.proto.cb.CallbackResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.findthinks.delay.job.proto.cb.JobCallbackReq,
      com.findthinks.delay.job.proto.cb.CallbackResp> getTriggerMethod() {
    io.grpc.MethodDescriptor<com.findthinks.delay.job.proto.cb.JobCallbackReq, com.findthinks.delay.job.proto.cb.CallbackResp> getTriggerMethod;
    if ((getTriggerMethod = DelayJobCallbackGrpc.getTriggerMethod) == null) {
      synchronized (DelayJobCallbackGrpc.class) {
        if ((getTriggerMethod = DelayJobCallbackGrpc.getTriggerMethod) == null) {
          DelayJobCallbackGrpc.getTriggerMethod = getTriggerMethod =
              io.grpc.MethodDescriptor.<com.findthinks.delay.job.proto.cb.JobCallbackReq, com.findthinks.delay.job.proto.cb.CallbackResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "trigger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.proto.cb.JobCallbackReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.findthinks.delay.job.proto.cb.CallbackResp.getDefaultInstance()))
              .setSchemaDescriptor(new DelayJobCallbackMethodDescriptorSupplier("trigger"))
              .build();
        }
      }
    }
    return getTriggerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DelayJobCallbackStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackStub>() {
        @java.lang.Override
        public DelayJobCallbackStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobCallbackStub(channel, callOptions);
        }
      };
    return DelayJobCallbackStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DelayJobCallbackBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackBlockingStub>() {
        @java.lang.Override
        public DelayJobCallbackBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobCallbackBlockingStub(channel, callOptions);
        }
      };
    return DelayJobCallbackBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DelayJobCallbackFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DelayJobCallbackFutureStub>() {
        @java.lang.Override
        public DelayJobCallbackFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DelayJobCallbackFutureStub(channel, callOptions);
        }
      };
    return DelayJobCallbackFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class DelayJobCallbackImplBase implements io.grpc.BindableService {

    /**
     */
    public void trigger(com.findthinks.delay.job.proto.cb.JobCallbackReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.proto.cb.CallbackResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTriggerMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getTriggerMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.findthinks.delay.job.proto.cb.JobCallbackReq,
                com.findthinks.delay.job.proto.cb.CallbackResp>(
                  this, METHODID_TRIGGER)))
          .build();
    }
  }

  /**
   */
  public static final class DelayJobCallbackStub extends io.grpc.stub.AbstractAsyncStub<DelayJobCallbackStub> {
    private DelayJobCallbackStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobCallbackStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobCallbackStub(channel, callOptions);
    }

    /**
     */
    public void trigger(com.findthinks.delay.job.proto.cb.JobCallbackReq request,
        io.grpc.stub.StreamObserver<com.findthinks.delay.job.proto.cb.CallbackResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTriggerMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DelayJobCallbackBlockingStub extends io.grpc.stub.AbstractBlockingStub<DelayJobCallbackBlockingStub> {
    private DelayJobCallbackBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobCallbackBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobCallbackBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.findthinks.delay.job.proto.cb.CallbackResp trigger(com.findthinks.delay.job.proto.cb.JobCallbackReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTriggerMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DelayJobCallbackFutureStub extends io.grpc.stub.AbstractFutureStub<DelayJobCallbackFutureStub> {
    private DelayJobCallbackFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DelayJobCallbackFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DelayJobCallbackFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.findthinks.delay.job.proto.cb.CallbackResp> trigger(
        com.findthinks.delay.job.proto.cb.JobCallbackReq request) {
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
    private final DelayJobCallbackImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DelayJobCallbackImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TRIGGER:
          serviceImpl.trigger((com.findthinks.delay.job.proto.cb.JobCallbackReq) request,
              (io.grpc.stub.StreamObserver<com.findthinks.delay.job.proto.cb.CallbackResp>) responseObserver);
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

  private static abstract class DelayJobCallbackBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DelayJobCallbackBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.findthinks.delay.job.proto.cb.DelayJobCallbackOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DelayJobCallback");
    }
  }

  private static final class DelayJobCallbackFileDescriptorSupplier
      extends DelayJobCallbackBaseDescriptorSupplier {
    DelayJobCallbackFileDescriptorSupplier() {}
  }

  private static final class DelayJobCallbackMethodDescriptorSupplier
      extends DelayJobCallbackBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DelayJobCallbackMethodDescriptorSupplier(String methodName) {
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
      synchronized (DelayJobCallbackGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DelayJobCallbackFileDescriptorSupplier())
              .addMethod(getTriggerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
