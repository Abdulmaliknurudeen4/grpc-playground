package com.nexusforge.grpcplayground.sec03;


import com.nexusforge.grpcplayground.models.common.Address;
import com.nexusforge.grpcplayground.models.common.BodyStyle;
import com.nexusforge.grpcplayground.models.common.Car;
import com.nexusforge.grpcplayground.models.sec04.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec09Import {
    public static final Logger log = LoggerFactory.getLogger(Lec09Import.class);

    public static void main(String[] args) {
        var address = Address.newBuilder().setCity("City").build();
        var car = Car.newBuilder().setBodyStyle(BodyStyle.COUPE).build();

        var person = Person.newBuilder()
                .setLastName("sam")
                .setAge(12)
                .setCar(car)
                .setAddress(address)
                .build();

        log.info("{}", person);
    }
}
