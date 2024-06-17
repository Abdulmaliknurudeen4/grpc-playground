package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01Scalar {
    private static final Logger log = LoggerFactory
            .getLogger(Lec01Scalar.class);

    public static void main(String[] args) {
      var person =Person.newBuilder()
                .setLastName("Nurudeen")
                .setAge(12)
                .setEmail("nurudeen@gmail.com")
                .setEmployed(true)
                .setSalary(1000.0)
                .setBankAccountNumber(928382377)
                .setBalance(-200735)
                .build();
      log.info("{}", person);
    }

}
