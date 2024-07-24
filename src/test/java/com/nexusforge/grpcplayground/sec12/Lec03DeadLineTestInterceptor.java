package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec12.AccountBalance;
import com.nexusforge.grpcplayground.models.sec12.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec12.Money;
import com.nexusforge.grpcplayground.models.sec12.WithdrawRequest;
import com.nexusforge.grpcplayground.sec12.interceptors.DeadLineInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec03DeadLineTestInterceptor extends AbstractInterceptorTest {
    private static final Logger log = LoggerFactory.getLogger(Lec03DeadLineTestInterceptor.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptor() {
        return List.of(
                new DeadLineInterceptor(Duration.ofSeconds(2))
        );
    }

    @Test
    public void blockingDeadlineTest() {


        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .getAccountBalance(request);

        log.info("{}", response);

    }

    @Test
    public void asyncDeadlineTest() {
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,
                Status.fromThrowable(observer.getThrowable()).getCode());
    }

    @Test
    public void ovverideInterceptorDemo() {
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();
        this.bankStub
                .withDeadline(Deadline.after(6, TimeUnit.SECONDS))
                .withdraw(request, observer);
        observer.await();
    }
}
