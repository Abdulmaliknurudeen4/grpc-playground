package com.nexusforge.grpcplayground.common;

import com.nexusforge.grpcplayground.sec06.BankService;

public class Demo {
    public static void main(String[] args) {
        GrpcServer.create(new BankService())
                .start()
                .await();
    }
}