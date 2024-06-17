package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lec02Serialization {
    public static final Path PATH = Path.of("person.out");
    private static final Logger log = LoggerFactory.getLogger(Lec02Serialization.class);

    public static void main(String[] args) throws IOException {

        var person = Person.newBuilder()
                .setLastName("Nurudeen")
                .setAge(12)
                .setEmail("nurudeen@gmail.com")
                .setEmployed(true)
                .setSalary(1000.0)
                .setBankAccountNumber(928382377)
                .setBalance(-200735)
                .build();
        log.info("{}", person);

        serialize(person);
        log.info("{}", deserialize());

        log.info("equals {}", person.equals(deserialize()));

        log.info("bytes length: {}", person.toByteArray().length);
    }

    public static void serialize(Person person) throws IOException {
        person.writeTo(Files.newOutputStream(PATH));
    }

    public static Person deserialize() throws IOException {
        return Person.parseFrom(Files.newInputStream(PATH));
    }
}
