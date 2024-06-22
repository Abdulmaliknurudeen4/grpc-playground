package com.nexusforge.grpcplayground.sec07;

import com.nexusforge.grpcplayground.models.sec07.Output;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ResponseHandler implements StreamObserver<Output> {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private int size;
    @Override
    public void onNext(Output output) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
