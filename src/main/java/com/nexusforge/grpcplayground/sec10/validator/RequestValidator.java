package com.nexusforge.grpcplayground.sec10.validator;

import com.nexusforge.grpcplayground.models.sec10.ErrorMessage;
import com.nexusforge.grpcplayground.models.sec10.ValidationCode;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;

import java.util.Optional;

public class RequestValidator {
    public static final Metadata.Key<ErrorMessage> ERROR_MESSAGE_KEY =
            ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

    public static Optional<StatusRuntimeException> validateAccount(int accountNumber) {
        if (accountNumber > 0 && accountNumber < 11) {
            return Optional.empty();
        }
        var metadata = toMetadata(ValidationCode.INVALID_ACCOUNT,
                "Invalid Account ID, The user Account does not exists");
        return Optional.of(Status.INVALID_ARGUMENT
                .withDescription("Invalid Account ID")
                .asRuntimeException(metadata));
    }

    public static Optional<StatusRuntimeException> isAmountDivisbleBy10(int amount) {
        if (amount > 0 && amount % 10 == 0) {
            return Optional.empty();
        }
        var metadata = toMetadata(ValidationCode.INVALID_AMOUNT,
                "The Amount you entered is invalid, Please ensure it's in 10 denominations and a positve integer");

        return Optional.of(Status.INVALID_ARGUMENT
                .withDescription("Requested amount should be in 10 denominations")
                .asRuntimeException(metadata));
    }


    public static Optional<StatusRuntimeException> hasSuffiecientBalance(int amount, int balance) {
        if (amount <= balance) {
            return Optional.empty();
        }
        var metadata = toMetadata(ValidationCode.INSUFFICIENT_BALANCE,
                "Insufficient Balance, Please top up");
        return Optional.of(Status.FAILED_PRECONDITION
                .withDescription("Your Account doesn't have enough balance to perform the withdrawal.")
                .asRuntimeException(metadata));
    }

    private static Metadata toMetadata(ValidationCode code, String description) {
        var metadata = new Metadata();
        var errorMessage = ErrorMessage.newBuilder()
                .setValidationCode(code)
                .setDescription(description)
                .build();
        metadata.put(ERROR_MESSAGE_KEY, errorMessage);
        return metadata;

    }

}
