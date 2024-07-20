package com.nexusforge.grpcplayground.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec11.AccountBalance;
import com.nexusforge.grpcplayground.models.sec11.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec11.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec02ServerStreamingDeadLineTest extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec02ServerStreamingDeadLineTest.class);

    @Test
    public void blockingDeadlineTest(){

       /* var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

       var iterator = this.bankBlockingStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request);

       while(iterator.hasNext()){
           log.info("{}", iterator.next());
       }*/

        try{
             var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

       var iterator = this.bankBlockingStub
                .withDeadline(Deadline.after(6, TimeUnit.SECONDS))
                .withdraw(request);

       while(iterator.hasNext()){
           log.info("{}", iterator.next());
       }
        }catch (Exception e){

        }
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
    }
}
