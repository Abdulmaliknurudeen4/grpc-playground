package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Address;
import com.nexusforge.grpcplayground.models.sec03.School;
import com.nexusforge.grpcplayground.models.sec03.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec04 {
    public static final Logger log = LoggerFactory.getLogger(Lec04.class);


    public static void main(String[] args) {
        //create student
        var address = Address.newBuilder()
                .setStreet("123 main str")
                .setCity("Atlanta")
                .setState("GA")
                .build();

        var student = Student.newBuilder()
                .setName("sam")
                .setAddress(address)
                .build();

        // create school
        var school = School.newBuilder()
                .setId(1)
                .setName("high school")
                .setAddress(address.toBuilder().setStreet("234 main st"))
                .build();

        log.info("school: {}", school);
        log.info("student: {}", student);
    }
}
