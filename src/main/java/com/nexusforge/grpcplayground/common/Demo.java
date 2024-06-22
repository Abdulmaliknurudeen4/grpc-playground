package com.nexusforge.grpcplayground.common;

import com.nexusforge.grpcplayground.sec06.BankService;
import com.nexusforge.grpcplayground.sec06.TransferService;

public class Demo {
    public static void main(String[] args) {
        GrpcServer.create(new BankService(), new TransferService())
                .start()
                .await();
    }
}
