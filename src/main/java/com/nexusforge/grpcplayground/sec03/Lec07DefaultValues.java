package com.nexusforge.grpcplayground.sec03;

import com.nexusforge.grpcplayground.models.sec03.Car;
import com.nexusforge.grpcplayground.models.sec03.Dealer;
import com.nexusforge.grpcplayground.models.sec03.Library;
import com.nexusforge.grpcplayground.models.sec03.School;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec07DefaultValues {
    public static final Logger log = LoggerFactory.getLogger(Lec07DefaultValues.class);

    public static void main(String[] args) {
        var school = School.newBuilder().build();

        log.info("{}", school.getId());
        log.info("{}", school.getName());
        log.info("{}", school.getAddress());
        log.info("{}", school.getAddress().getCity());


        log.info(" has address {}", school.hasAddress());

        var lib = Library.newBuilder().build();
        log.info("{}", lib.getBooksList());

        var dealer = Dealer.newBuilder().build();
        log.info("{}", dealer.getInventoryCount());

        var car = Car.newBuilder().build();
        log.info("{}", car.getBodyStyle());

    }
}
