package com.nexusforge.grpcplayground.sec08;

import com.google.common.util.concurrent.Uninterruptibles;
import com.nexusforge.grpcplayground.models.sec08.GuessRequest;
import com.nexusforge.grpcplayground.models.sec08.GuessResponse;
import com.nexusforge.grpcplayground.models.sec08.Result;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ResponseHandler implements StreamObserver<GuessResponse> {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private int upperBound = 100;
    private int lowerBound = 0;
    private int guessNumber;

    private StreamObserver<GuessRequest> requestObserver;

    @Override
    public void onNext(GuessResponse guessResponse) {
        process(guessResponse);

        if (guessResponse.getResult() == Result.CORRECT) {
            log.info("client guessedNumber is correct");
            this.onCompleted();
        } else if (guessResponse.getResult() == Result.TOO_HIGH) {
            upperBound = guessNumber;
            guessNumber = (lowerBound + upperBound) / 2;
        } else if (guessResponse.getResult() == Result.TOO_LOW) {
            lowerBound = guessNumber;
            guessNumber = (lowerBound + upperBound) / 2;
        }
        this.play(guessNumber);
        log.info("===========================");
        log.info("client is guess again with Guess Number {}", guessNumber);
        log.info("upper Bound is {}, lower bound is {}", upperBound, lowerBound);
    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.requestObserver.onCompleted();
        log.info("completed");
        latch.countDown();
    }

    public void setRequestObserver(StreamObserver<GuessRequest> requestObserver) {
        this.requestObserver = requestObserver;
    }

    public void await() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void play(int clientGuess) {
        log.info("client play with Number {} ", clientGuess);
        this.guessNumber = clientGuess;
        this.requestObserver.onNext(
                GuessRequest.newBuilder()
                        .setGuess(guessNumber).build());
    }

    private void process(GuessResponse response) {
        log.info("received {}", response);
        var statusGuess = response.getResult();
        var attempts = response.getAttempt();
        log.info("Client Guess is {}", statusGuess);
        log.info("Client Attempts is {}", attempts);
        Uninterruptibles.sleepUninterruptibly(
                ThreadLocalRandom.current().nextInt(50, 200),
                TimeUnit.MILLISECONDS
        );
    }

    public void start() {
        this.play(ThreadLocalRandom.current().nextInt(lowerBound, upperBound));
    }
}
