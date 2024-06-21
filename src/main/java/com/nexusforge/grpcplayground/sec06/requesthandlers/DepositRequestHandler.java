package com.nexusforge.grpcplayground.sec06.requesthandlers;

import com.nexusforge.grpcplayground.models.sec06.AccountBalance;
import com.nexusforge.grpcplayground.models.sec06.DepositRequest;
import com.nexusforge.grpcplayground.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {

    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;
    private static final Logger log = LoggerFactory.getLogger(DepositRequestHandler.class);

    public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        switch (depositRequest.getRequestCase()) {
            case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
            case MONEY -> AccountRepository.addAmount(this.accountNumber, depositRequest.getMoney().getAmount());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        // when the client wants to cancel the message.
        // perform DB rollbacks
        log.info("client error {} ", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        // done with deposit. //commits
        var accountBalance = AccountBalance.newBuilder().setAccountNumber(this.accountNumber)
                .setBalance(AccountRepository.getBalance(accountNumber))
                .build();
        this.responseObserver.onNext(accountBalance);
        this.responseObserver.onCompleted();
    }
}
