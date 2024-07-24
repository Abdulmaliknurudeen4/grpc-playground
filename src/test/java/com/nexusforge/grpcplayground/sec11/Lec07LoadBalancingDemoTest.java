package com.nexusforge.grpcplayground.sec11;

import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec07LoadBalancingDemoTest extends AbstractChannelTest {
    private static final Logger log = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    private ManagedChannel channel;

    @BeforeAll
    public void setup() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8585)
                .usePlaintext()
                .build();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void eagerChannelDemo() {


        for (int i = 1; i < 10; i++) {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();

            var response = bankBlockingStub.getAccountBalance(request);
            log.info("{}", response);
        }
    }

    @AfterAll
    public void stop() {
        this.channel.shutdown();
    }

}
