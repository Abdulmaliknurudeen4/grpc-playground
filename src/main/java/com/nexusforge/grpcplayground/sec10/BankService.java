package com.nexusforge.grpcplayground.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.models.sec10.*;
import com.nexusforge.grpcplayground.sec10.repository.AccountRepository;
import com.nexusforge.grpcplayground.sec10.validator.RequestValidator;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        RequestValidator.validateAccount(request.getAccountNumber())
                .ifPresentOrElse(
                        responseObserver::onError,
                        () -> sendAccountBalance(request, responseObserver)
                );
    }

    private void sendAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

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
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        RequestValidator.validateAccount(request.getAccountNumber())
                //chaninig validations
                .or(() -> RequestValidator.isAmountDivisbleBy10(request.getAmount()))
                .or(() -> RequestValidator.hasSuffiecientBalance(request.getAmount(), AccountRepository.getBalance(request.getAccountNumber())))
                .ifPresentOrElse(
                        responseObserver::onError,
                        () -> {
                            sendMoney(request, responseObserver);
                        }
                );
    }

    private void sendMoney(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        // do validations, amounts must be in 10 denominations (20 30 40)
        //account must be valid.

        try {
            var accountNumber = request.getAccountNumber();
            var requestAmount = request.getAmount();

            var accountBalance = AccountRepository.getBalance(accountNumber);
            if (requestAmount > accountBalance) {
                responseObserver.onCompleted();
                return;
            }

            for (int i = 0; i < requestAmount / 10; i++) {
                var money = Money.newBuilder().setAmount(10).build();
                if (i == 3) {
                    throw new RuntimeException();
                }
                responseObserver.onNext(money);
                log.info("money sent {} ", money);
                AccountRepository.deductAmount(accountNumber, 10);
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            // incase of server errrors
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Oops").asRuntimeException()
            );
        }
    }

}