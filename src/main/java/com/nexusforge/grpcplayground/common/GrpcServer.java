package com.nexusforge.grpcplayground.common;

import com.nexusforge.grpcplayground.sec06.BankService;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        var server = ServerBuilder.forPort(6565)
                .addService(new BankService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
