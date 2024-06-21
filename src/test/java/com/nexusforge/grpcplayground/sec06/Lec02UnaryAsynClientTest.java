package com.nexusforge.grpcplayground.sec06;

import com.google.protobuf.Empty;
import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.AllAccountResponse;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class Lec02UnaryAsynClientTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsynClientTest.class);

    @Test
    public void getBalanceTest() throws InterruptedException {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var latch = new CountDownLatch(1);
        this.stub.getAccountBalance(request, new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("Async balance received: {}", accountBalance);
                Assertions.assertEquals(100, accountBalance.getBalance());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {

            }
        });
        latch.await();
    }

    @Test
    public void getAllAccounts() throws InterruptedException {
        var request = AllAccountResponse.newBuilder()
                .build();
        var latch = new CountDownLatch(1);
       this.stub.getAllAccounts(Empty.newBuilder().build(), new StreamObserver<AllAccountResponse>() {
           @Override
           public void onNext(AllAccountResponse allAccountResponse) {
               log.info("unary All Accounts received: {} ", allAccountResponse != null);
               Assertions.assertEquals(10, allAccountResponse.getAccountsList().size());
               latch.countDown();
           }

           @Override
           public void onError(Throwable throwable) {
               latch.countDown();
           }

           @Override
           public void onCompleted() {

           }
       });
       latch.await();

    }
}
