package com.nexusforge.grpcplayground.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec11.BankServiceGrpc;
import com.nexusforge.grpcplayground.models.sec11.WithdrawRequest;
import io.grpc.Deadline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec03WaitForReadyLineTest extends AbstractChannelTest {
    private static final Logger log = LoggerFactory.getLogger(Lec03WaitForReadyLineTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new DelayedBankService());
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

    @BeforeAll
    public void setup() {
        Runnable runnable = () -> {
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
            this.grpcServer.start();
        };
        Thread.ofVirtual().start(runnable);
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

    @Test
    public void blockingDeadlineTest() {


        var request = WithdrawRequest
                .newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        var response = this.bankBlockingStub
                .withWaitForReady()
                .withDeadline(Deadline.after(15, TimeUnit.SECONDS))
                .withdraw(request);

        while (response.hasNext()) {
            log.info("{}", response.next());
        }

        log.info("{}", response);


    }
}
