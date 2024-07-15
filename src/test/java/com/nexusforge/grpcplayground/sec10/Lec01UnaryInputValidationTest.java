package com.nexusforge.grpcplayground.sec10;

import com.nexusforge.grpcplayground.common.ResponseObserver;
import com.nexusforge.grpcplayground.models.sec10.AccountBalance;
import com.nexusforge.grpcplayground.models.sec10.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec10.ErrorMessage;
import com.nexusforge.grpcplayground.models.sec10.ValidationCode;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryInputValidationTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec01UnaryInputValidationTest.class);

    @Test
    public void blockingInputValidationTest() {

        var ex = Assertions.assertThrows(StatusRuntimeException.class,
                () -> {
                    var request = BalanceCheckRequest.newBuilder()
                            .setAccountNumber(11)
                            .build();
                    var response = this.bankBlockingStub.getAccountBalance(request);

                });
        var key = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(ex));

    }

    @Test
    public void asyncInputValidtion() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(11)
                .build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.bankStub.getAccountBalance(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(observer.getThrowable()));
    }
}
