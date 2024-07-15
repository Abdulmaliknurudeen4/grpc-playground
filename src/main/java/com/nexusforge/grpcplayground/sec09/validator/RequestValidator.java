package com.nexusforge.grpcplayground.sec09.validator;

import io.grpc.Status;

import java.util.Optional;

public class RequestValidator {
    public static Optional<Status> validateAccount(int accountNumber){
        if(accountNumber > 0 && accountNumber < 11){
            return Optional.empty();
        }
        return Optional.of(Status.INVALID_ARGUMENT.withDescription("Invalid Account ID"));
    }

    public static Optional<Status> isAmountDivisbleBy10(int amount){
        if(amount > 0 && amount%10 ==0){
            return Optional.empty();
        }
        return Optional.of(Status.INVALID_ARGUMENT.withDescription("Requested amount should be in 10 denominations"));
    }


    public static Optional<Status> hasSuffiecientBalance(int amount, int balance){
        if(amount <= balance){
            return Optional.empty();
        }
        return Optional.of(Status.FAILED_PRECONDITION.withDescription("Your Account doesn't have enough balance to perform the withdrawal."));
    }

}
