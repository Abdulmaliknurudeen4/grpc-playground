package com.nexusforge.grpcplayground.sec06;

import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
import com.nexusforge.grpcplayground.models.sec06.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var accountBalance = AccountBalance.newBuilder().
                setAccountNumber(accountNumber)
                .setBalance(accountNumber * 10)
                .build();
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }
}
