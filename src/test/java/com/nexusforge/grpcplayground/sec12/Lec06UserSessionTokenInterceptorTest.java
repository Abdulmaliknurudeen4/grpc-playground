package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.sec12.Interceptors.ApiKeyValidationInterceptor;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest{
    private static final Logger log = LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptor() {
        return Collections.emptyList();
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer
                .create(6565, serverBuilder -> {
                    serverBuilder.addService(new BankService())
                            .intercept(new UserSessionTokenInter());
                });
    }


    private Metadata getApiKey() {
        var metadata = new Metadata();
        metadata.put(Constants.API_KEY, "bank-client-secret");
        return metadata;
    }

    @Test
    public void userCredentialsDemo() {


        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .withCallCredentials(new UserSessionToken(""))
                .getAccountBalance(request);

        log.info("{}", response);

    }

    private static class UserSessionToken extends CallCredentials{

        private final String jwt;
        private static final String TOKEN_FORMAT = "%s %s";

        public UserSessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo,
                                         Executor executor, MetadataApplier metadataApplier) {

            //incase a long running jwt generation shi
            executor.execute(()->{
                var metadata = new Metadata();
                // Autorization Bearer criptyic
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
                metadataApplier.apply(metadata);
            });
        }
    }
}
