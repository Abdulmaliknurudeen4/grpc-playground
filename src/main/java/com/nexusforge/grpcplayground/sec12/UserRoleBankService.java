package com.nexusforge.grpcplayground.sec12;

import com.nexusforge.grpcplayground.models.sec12.*;
import com.nexusforge.grpcplayground.sec12.repository.AccountRepository;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoleBankService extends BankServiceGrpc.BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(UserRoleBankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        var currentCtx = Context.current();
        // the constants java file gets the user-key from the context. so
        log.info("{}", Constants.USER_ROLE_KEY.get());
        // this would always return true because it's always true
        // we're not getting the value from the context.
        // the context isn't present in this service whatsoever

        // this line is not always true.
        if(UserRole.STANDARD.equals(Constants.USER_ROLE_KEY.get())){
            var fee = balance > 0 ? 1 :0;
            AccountRepository.deductAmount(accountNumber, fee);
            balance = balance - fee;
        }

        var accountBalance = AccountBalance.newBuilder().
                setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();

        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

}