package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec12.BankServiceGrpc;
import com.nexusforge.grpcplayground.sec12.Interceptors.GzipResponseInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractInterceptorTest {
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    protected BankServiceGrpc.BankServiceStub bankStub;
    protected ManagedChannel channel;
    private GrpcServer grpcServer;

    protected abstract List<ClientInterceptor> getClientInterceptor();

    protected GrpcServer createServer() {
        return GrpcServer
                .create(6565, serverBuilder -> {
                    serverBuilder.addService(new BankService())
                            .intercept(new GzipResponseInterceptor());
                });
    }

    @BeforeAll
    public void setup() {
        this.grpcServer = createServer();
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
