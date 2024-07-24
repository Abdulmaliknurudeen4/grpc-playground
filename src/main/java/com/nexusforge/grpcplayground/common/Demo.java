package com.nexusforge.grpcplayground.common;

import com.nexusforge.grpcplayground.sec12.BankService;
import com.nexusforge.grpcplayground.sec12.Interceptors.ApiKeyValidationInterceptor;

public class Demo {
    public static void main(String[] args) {
      /*  GrpcServer.create(new BankService())
                .start()
                .await();*/
        GrpcServer
                .create(6565, serverBuilder -> {
                    serverBuilder.addService(new BankService())
                            .intercept(new ApiKeyValidationInterceptor());
                }).start().await();
    }

    /*private static class BankInstance1{
        public static void main(String[] args) {
            GrpcServer.create(6565, new BankService())
                    .start().await();
        }
    }

    private static class BankInstance2{
        public static void main(String[] args) {
            GrpcServer.create(7575, new BankService())
                    .start().await();
        }
    }*/
}
