package com.nexusforge.grpcplayground.sec03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nexusforge.grpcplayground.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Lec03PerformanceTest {
    public static final Logger log = LoggerFactory.getLogger(Lec03PerformanceTest.class);
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        var protoPerson = Person.newBuilder()
                .setLastName("Nurudeen")
                .setAge(12)
                .setEmail("nurudeen@gmail.com")
                .setEmployed(true)
                .setSalary(1000.0)
                .setBankAccountNumber(928382377)
                .setBalance(-200735)
                .build();
        var jsonPerson = new JsonPerson(
                "Nurudeen",
                12,
                "nurudeen@gmail.com",
                true,
                1000.0,
                928382377,
                -200735

        );

        for(int i = 0; i <5; i++){
            runTest("json", ()->json(jsonPerson));
            runTest("proto", ()->proto(protoPerson));
        }

    }

    public static void proto(Person person) {
        try {
            byte[] byteArray = person.toByteArray();
            Person.parseFrom(byteArray);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static void json(JsonPerson person) {
        try {
            var bytes = mapper.writeValueAsBytes(person);
            mapper.readValue(bytes, JsonPerson.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runTest(String testName, Runnable runnable) {
        var start = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            runnable.run();
        }
        var end = System.currentTimeMillis();
        log.info("time take for {} - {}", testName, (end-start));
    }
}
