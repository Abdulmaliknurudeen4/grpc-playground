package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.sec12.Interceptors.ApiKeyValidationInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec05ClientApiKeyInterceptorTest extends AbstractInterceptorTest {
private static final Logger log = LoggerFactory.getLogger(Lec05ClientApiKeyInterceptorTest.class);
    @Override
    protected List<ClientInterceptor> getClientInterceptor() {
        return List.of(
                MetadataUtils.newAttachHeadersInterceptor(getApiKey())
        );
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer
                .create(6565, serverBuilder -> {
                    serverBuilder.addService(new BankService())
                            .intercept(new ApiKeyValidationInterceptor());
                });
    }


    private Metadata getApiKey() {
        var metadata = new Metadata();
        metadata.put(Constants.API_KEY, "bank-client-secret");
        return metadata;
    }

    @Test
    public void clientApiKey() {


        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .getAccountBalance(request);

        log.info("{}", response);

    }
}
