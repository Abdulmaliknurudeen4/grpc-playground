package com.nexusforge.grpcplayground.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec11.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec11.BankServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec04LazyChannelDemoTest extends AbstractChannelTest {
    private static final Logger log = LoggerFactory.getLogger(Lec04LazyChannelDemoTest.class);

    private final GrpcServer grpcServer = GrpcServer.create(new DelayedBankService());
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

    @BeforeAll
    public void setup() {
        //
        //this.grpcServer.start();
        this.bankBlockingStub = BankServiceGrpc
                .newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

    @Test
    public void lazyChannelDemo(){
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        var response = this.bankBlockingStub.getAccountBalance(request);
        log.info("{}", response);
    }
}
