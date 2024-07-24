package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec12.BankServiceGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractInterceptorTest {
    private final GrpcServer grpcServer = GrpcServer
            .create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    protected BankServiceGrpc.BankServiceStub bankStub;
    protected ManagedChannel channel;

    protected abstract List<ClientInterceptor> getClientInterceptor();


    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .intercept(getClientInterceptor())
                .build();
        this.bankStub = BankServiceGrpc.newStub(channel);
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {

        this.grpcServer.stop();
        this.channel.shutdown();
    }
}
