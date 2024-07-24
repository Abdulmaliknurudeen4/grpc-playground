package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec12.Money;
import com.nexusforge.grpcplayground.models.sec12.WithdrawRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class Lec02ExecutorCallOptionTest extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec02ExecutorCallOptionTest.class);

    @Test
    public void gzipDemo(){
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();
        this.bankStub
                .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
                .withdraw(request, observer);
        observer.await();

        //using virtual Threads to make IO Calls like inserts to the
        // database and etc.
    }
}
