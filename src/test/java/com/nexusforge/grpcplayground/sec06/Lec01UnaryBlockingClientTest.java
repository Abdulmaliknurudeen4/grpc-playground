package com.nexusforge.grpcplayground.sec06;

import com.google.protobuf.Empty;
import com.nexusforge.grpcplayground.models.sec06.AllAccountResponse;
import com.nexusforge.grpcplayground.models.sec06.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryBlockingClientTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec01UnaryBlockingClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        var balance = this.bankBlockingStub.getAccountBalance(request);
        log.info("unary Balance received: {}", balance);
        Assertions.assertEquals(100, balance.getBalance());
    }

    @Test
    public void getAllAccounts(){
        var request = AllAccountResponse.newBuilder()
                .build();
        var accounts = this.bankBlockingStub.getAllAccounts(Empty.newBuilder().build());
        log.info("unary All Accounts received: {} ", accounts != null);
        Assertions.assertEquals(10, accounts.getAccountsList().size());
    }
}
