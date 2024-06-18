package com.nexusforge.grpcplayground.sec05.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import com.nexusforge.grpcplayground.models.sec05.v2.Television;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V2Parser {
    public static final Logger log = LoggerFactory.getLogger(V2Parser.class);

    public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
        var tv = Television.parseFrom(bytes);
        log.info("brand: {}", tv.getBrand());
        log.info("year: {}", tv.getModel());
        log.info("type: {}", tv.getType());
    }

}
