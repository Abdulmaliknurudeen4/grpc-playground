package com.nexusforge.grpcplayground.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.models.sec11.*;
import com.nexusforge.grpcplayground.sec11.repository.AccountRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DelayedBankService extends BankServiceGrpc.BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(DelayedBankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        var accountBalance = AccountBalance.newBuilder().
                setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }


    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        // do validations, amounts must be in 10 denominations (20 30 40)
        //account must be valid.

        var accountNumber = request.getAccountNumber();
        var requestAmount = request.getAmount();

        var accountBalance = AccountRepository.getBalance(accountNumber);
        if (requestAmount > accountBalance) {
//            responseObserver.onCompleted();
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException());
            return;
        }

        for (int i = 0; i < requestAmount / 10; i++) {
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("money sent {} ", money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        }
        responseObserver.onCompleted();
    }

}