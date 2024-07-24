package com.nexusforge.grpcplayground.sec12.interceptors;

import io.grpc.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DeadLineInterceptor implements ClientInterceptor {
    private final Duration duration;

    public DeadLineInterceptor(Duration duration) {
        this.duration = duration;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        callOptions = callOptions.withDeadline(Deadline.after(duration.toMillis(), TimeUnit.MILLISECONDS));
        return channel.newCall(methodDescriptor, callOptions);
    }
}
