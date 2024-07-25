package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec12.Money;
import com.nexusforge.grpcplayground.models.sec12.WithdrawRequest;
import com.nexusforge.grpcplayground.sec12.Interceptors.UserSessionTokenInterceptor;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest{
    private static final Logger log = LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptor() {
        return Collections.emptyList();
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer
                .create(6565, serverBuilder -> serverBuilder.addService(new BankService())
                        .intercept(new UserSessionTokenInterceptor()));
    }


  /*  private Metadata getApiKey() {
        var metadata = new Metadata();
        metadata.put(Constants.API_KEY, "bank-client-secret");
        return metadata;
    }*/

    @Test
    public void unaryUserCredentialsDemo() {

        for (int i = 1; i <= 5; i++) {

            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();

            var response = this.bankBlockingStub
                    .withCallCredentials(new UserSessionToken("user-token-"+i))
                    .getAccountBalance(request);

            log.info("{}", response);
        }

    }

    @Test
    public void streamingUserCredentialsDemo() {

        for (int i = 1; i <= 5; i++) {

            var observer = ResponseObserver.<Money>create();
            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(i)
                    .setAmount(30)
                    .build();
            this.bankStub
                    .withCallCredentials(new UserSessionToken("user-token-"+i))
                    .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
                    .withdraw(request, observer);
            observer.await();
        }

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
