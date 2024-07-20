package com.nexusforge.grpcplayground.sec10;

import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec10.Money;
import com.nexusforge.grpcplayground.models.sec10.ValidationCode;
import com.nexusforge.grpcplayground.models.sec10.WithdrawRequest;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Lec02ServerStreamingTest extends AbstractTest {

    @ParameterizedTest
    @MethodSource("testdata")
    public void blockingInputValidationTest(WithdrawRequest request, ValidationCode code) {

        var ex = Assertions.assertThrows(StatusRuntimeException.class,
                () -> {

                    var response = this.bankBlockingStub.withdraw(request).hasNext();

                });

        Assertions.assertEquals(code, getValidationCode(ex));

    }

    private Stream<Arguments> testdata() {
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), ValidationCode.INVALID_ACCOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(), ValidationCode.INVALID_AMOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(), ValidationCode.INSUFFICIENT_BALANCE)
        );
    }

    @ParameterizedTest
    @MethodSource("testdata")
    public void asyncInputValidtion(WithdrawRequest request, ValidationCode code) {

        var observer = ResponseObserver.<Money>create();
        this.bankStub.withdraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, getValidationCode(observer.getThrowable()));
    }

}
