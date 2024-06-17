package com.nexusforge.grpcplayground.sec01;

import com.nexusforge.grpcplayground.models.sec01.PersonOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProtoDemo {
    private static final Logger log = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {

        var sam = PersonOuterClass.Person.newBuilder()
                .setName("sam")
                .setAge(12)
                .build();
        log.info("{}", sam);

    }
}
