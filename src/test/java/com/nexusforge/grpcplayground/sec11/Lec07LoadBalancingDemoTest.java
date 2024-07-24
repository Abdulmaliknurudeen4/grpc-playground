package com.nexusforge.grpcplayground.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec06.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Lec07LoadBalancingDemoTest extends AbstractChannelTest {
    private static final Logger log = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    private BankServiceGrpc.BankServiceStub bankStub;
    private ManagedChannel channel;

    @BeforeAll
    public void setup() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8585)
                .usePlaintext()
                .build();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.bankStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void loadBalancingDemo() {


        for (int i = 1; i < 10; i++) {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();

            var response = bankBlockingStub.getAccountBalance(request);
            log.info("{}", response);
        }
    }

    @Test
    public void clientStreamingTest(){
        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.bankStub.deposit(responseObserver);

        //initial message - accountNumber
        requestObserver.onNext(DepositRequest.newBuilder()
                .setAccountNumber(5)
                .build());

        IntStream.rangeClosed(1, 30)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(c -> DepositRequest.newBuilder().setMoney(c).build())
                .forEach(d ->{
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                    requestObserver.onNext(d);
                });


        requestObserver.onCompleted();

        //at this point our response observer should receive a response
        responseObserver.await();
    }

    @AfterAll
    public void stop() {
        this.channel.shutdown();
    }

}
