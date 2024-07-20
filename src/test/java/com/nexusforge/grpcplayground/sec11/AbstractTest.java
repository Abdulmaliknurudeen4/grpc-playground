package com.nexusforge.grpcplayground.sec11;

import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec11.BankServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {
    private final GrpcServer grpcServer = GrpcServer
            .create(new DelayedBankService());
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    protected BankServiceGrpc.BankServiceStub bankStub;

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.bankStub = BankServiceGrpc.newStub(channel);
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

}
