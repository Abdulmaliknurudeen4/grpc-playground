package com.nexusforge.grpcplayground.sec06;

import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class GrpcClient {
    public static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) throws InterruptedException {

        var channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        var stub = BankServiceGrpc.newStub(channel);

        // newStub - Asynch is the only one that supports 4 communication
        // patterns

        stub.getAccountBalance(BalanceCheckRequest
                .newBuilder()
                .setAccountNumber(2)
                .build(), new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("{}", accountBalance);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.info("completed.");
            }
        });

        log.info("Done");
        Thread.sleep(Duration.ofSeconds(5));
    }

}
