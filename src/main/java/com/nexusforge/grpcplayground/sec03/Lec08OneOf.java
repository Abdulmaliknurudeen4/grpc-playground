package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Credentials;
import com.nexusforge.grpcplayground.models.sec03.Email;
import com.nexusforge.grpcplayground.models.sec03.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec08OneOf {
    public static final Logger log = LoggerFactory.getLogger(Lec08OneOf.class);

    public static void main(String[] args) {

        var email = Email.newBuilder().setAddress("Sam@gmail.com")
                .setPassword("admin")
                .build();
        var phone = Phone.newBuilder().setNumber(12347809).setCode(234).build();

        login(Credentials.newBuilder()
                .setEmail(email)
                .build());
        login(Credentials.newBuilder()
                .setPhone(phone)
                .build());

    }

    private static void login(Credentials credentials) {
        switch (credentials.getLoginTypeCase()) {
            case EMAIL -> log.info("email ->", credentials.getEmail());
            case PHONE -> log.info("phone ->", credentials.getPhone());
        }
    }
}
