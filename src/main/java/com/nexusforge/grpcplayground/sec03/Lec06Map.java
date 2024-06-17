package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.BodyStyle;
import com.nexusforge.grpcplayground.models.sec03.Car;
import com.nexusforge.grpcplayground.models.sec03.Dealer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Lec06Map {
    public static final Logger log = LoggerFactory.getLogger(Lec04.class);

    public static void main(String[] args) {
        var car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("civic")
                .setYear(2000)
                .setBodyStyle(BodyStyle.SUV)
                .build();

        var car2 = Car.newBuilder()
                .setMake("Honda")
                .setModel("accord")
                .setYear(2030)
                .setBodyStyle(BodyStyle.COUPE)
                .build();

        var dealer = Dealer.newBuilder()
                .putAllInventory(Map.of(car1.getYear(), car1,
                        car2.getYear(), car2))
                .build();

        log.info("{}", dealer);

    }
}
