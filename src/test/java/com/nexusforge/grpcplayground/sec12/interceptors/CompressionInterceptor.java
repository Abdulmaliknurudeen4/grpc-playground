package com.nexusforge.grpcplayground.sec12.interceptors;

import io.grpc.*;

import java.util.Objects;

public class CompressionInterceptor implements ClientInterceptor {
    private final String compression;

    public CompressionInterceptor(String compression) {
        this.compression = compression;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        //Option to evade compression

        callOptions = (Objects.equals(callOptions.getCompressor(), "identity"))
                ? callOptions :
                callOptions.withCompression(compression);
        return channel.newCall(methodDescriptor, callOptions);
    }
}
