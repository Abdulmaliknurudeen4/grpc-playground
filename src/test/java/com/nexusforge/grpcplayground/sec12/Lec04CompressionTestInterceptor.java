package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.sec12.interceptors.CompressionInterceptor;
import io.grpc.ClientInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec04CompressionTestInterceptor extends AbstractInterceptorTest {
    private static final Logger log = LoggerFactory.getLogger(Lec04CompressionTestInterceptor.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptor() {
        return List.of(
                new CompressionInterceptor("gzip")
        );
    }

    @Test
    public void gzip() {


        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .getAccountBalance(request);

        log.info("{}", response);

    }

}
