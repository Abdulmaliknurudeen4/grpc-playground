package com.nexusforge.grpcplayground.sec06;

import com.google.protobuf.Empty;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.AllAccountResponse;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
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
        var observer = ResponseObserver.<AccountBalance>create();
        this.stub.getAccountBalance(request, observer);
        observer.await();
        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(100, observer.getItems().getFirst().getBalance());
        Assertions.assertNull(observer.getThrowable());

    }

    @Test
    public void getAllAccounts() throws InterruptedException {
        var request = AllAccountResponse.newBuilder()
                .build();
        var observer = ResponseObserver.<AllAccountResponse>create();
        this.stub.getAllAccounts(Empty.newBuilder().build(), observer);
        observer.await();
        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().getFirst().getAccountsCount());
        Assertions.assertNull(observer.getThrowable());
    }
}
