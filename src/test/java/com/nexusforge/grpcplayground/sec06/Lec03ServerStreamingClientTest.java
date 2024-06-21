package com.nexusforge.grpcplayground.sec06;

import com.google.protobuf.Empty;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec06.AllAccountResponse;
import com.nexusforge.grpcplayground.models.sec06.Money;
import com.nexusforge.grpcplayground.models.sec06.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Lec03ServerStreamingClientTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);


    @Test
    public void blockingClientWithdrawTest() {
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(2)
                .setAmount(20)
                .build();
        var iterator = this.blockingStub.withdraw(request);
        int count = 0;
        while(iterator.hasNext()){
            log.info("Received money: {}", iterator.next());
            count++;
        }

        Assertions.assertEquals(2, count);
    }

    @Test
    public void asyncClientWithdrawTest(){
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(9)
                .setAmount(50)
                .build();

        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request, observer);
        observer.await();
        Assertions.assertEquals(5, observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().getFirst().getAmount());
        Assertions.assertNull(observer.getThrowable());

    }
}
