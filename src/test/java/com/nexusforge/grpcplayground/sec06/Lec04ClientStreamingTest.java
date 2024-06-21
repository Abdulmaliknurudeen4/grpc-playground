package com.nexusforge.grpcplayground.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.DepositRequest;
import com.nexusforge.grpcplayground.models.sec06.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Lec04ClientStreamingTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec04ClientStreamingTest.class);

    @Test
    public void depositTest() {
        //this.blockingStub -> blocking stub doesn't support client stream
        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.stub.deposit(responseObserver);

        //initial message - accountNumber
        requestObserver.onNext(DepositRequest.newBuilder()
                .setAccountNumber(5)
                .build());

        // client cancelling out
        /*Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        requestObserver.onError(new RuntimeException());*/
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(c -> DepositRequest.newBuilder().setMoney(c).build())
                .forEach(requestObserver::onNext);


        requestObserver.onCompleted();

        //at this point our response observer should receive a response
        responseObserver.await();

        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(200, responseObserver.getItems().getFirst().getBalance());
        Assertions.assertNull(responseObserver.getThrowable());
    }

}
