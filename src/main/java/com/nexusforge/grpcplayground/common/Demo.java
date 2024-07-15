package com.nexusforge.grpcplayground.common;

import com.nexusforge.grpcplayground.sec10.BankService;

public class Demo {
    public static void main(String[] args) {
        GrpcServer.create(new BankService())
                .start()
                .await();
    }/* public static void main(String[] args) {
        GrpcServer.create(new FlowControlService())
                .start()
                .await();
    }*/
    /*public static void main(String[] args) {
        GrpcServer.create(new BankService(), new TransferService())
                .start()
                .await();
    }*/
}
