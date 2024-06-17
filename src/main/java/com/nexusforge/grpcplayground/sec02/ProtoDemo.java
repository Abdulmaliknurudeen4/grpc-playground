package com.nexusforge.grpcplayground.sec02;

import com.nexusforge.grpcplayground.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {
    private static final Logger log = LoggerFactory.getLogger(ProtoDemo.class);

    public static void main(String[] args) {
        Person sam = Person.newBuilder()
                .setName("sam")
                .setAge(45)
                .build();
        log.info("{}", sam);


    }
}
