package com.nexusforge.grpcplayground.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import com.nexusforge.grpcplayground.models.sec06.*;
import com.nexusforge.grpcplayground.sec06.repository.AccountRepository;
import com.nexusforge.grpcplayground.sec06.requesthandlers.DepositRequestHandler;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        var accountBalance = AccountBalance.newBuilder().
                setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAccounts(Empty request, StreamObserver<AllAccountResponse> responseObserver) {
        var accounts = AccountRepository.getAllAccounts()
                .entrySet()
                .stream().map(e -> AccountBalance.newBuilder()
                        .setAccountNumber(e.getKey())
                        .setBalance(e.getValue())
                        .build())
                .toList();
        var response = AllAccountResponse.newBuilder()
                .addAllAccounts(accounts)
                .build();
        responseObserver.onNext(response);
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
            responseObserver.onCompleted();
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

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalance> responseObserver) {
        return new DepositRequestHandler(responseObserver);
    }
}