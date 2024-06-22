package com.nexusforge.grpcplayground.sec06;

import com.nexusforge.grpcplayground.models.sec06.TransferRequest;
import com.nexusforge.grpcplayground.models.sec06.TransferResponse;
import com.nexusforge.grpcplayground.models.sec06.TransferServiceGrpc;
import com.nexusforge.grpcplayground.sec06.requesthandlers.TransferRequestHandler;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferRequestHandler(responseObserver);
    }
}
