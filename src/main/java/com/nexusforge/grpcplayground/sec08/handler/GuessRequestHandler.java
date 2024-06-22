package com.nexusforge.grpcplayground.sec08.handler;

import com.nexusforge.grpcplayground.models.sec08.GuessRequest;
import com.nexusforge.grpcplayground.models.sec08.GuessResponse;
import com.nexusforge.grpcplayground.models.sec08.Result;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class GuessRequestHandler implements StreamObserver<GuessRequest> {
    private static final Logger log = LoggerFactory.getLogger(GuessRequestHandler.class);
    private final StreamObserver<GuessResponse> responseObserver;
    private final Integer guessedNumber;
    private int attempts;

    public GuessRequestHandler(StreamObserver<GuessResponse> responseObserver) {
        this.responseObserver = responseObserver;
        this.guessedNumber = ThreadLocalRandom.current().nextInt(1, 101);
        log.info("Guessed Number on the Server is {}", guessedNumber);
        attempts = 0;
    }

    @Override
    public void onNext(GuessRequest guessRequest) {
        attempts++;
        var guessStatus = this.guessHint(guessRequest);
        var response = GuessResponse.newBuilder()
                .setAttempt(attempts)
                .setResult(guessStatus)
                .build();
        responseObserver.onNext(response);
// remove if need be
        if (response.getResult() == Result.CORRECT) {
            this.onCompleted();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("client error: {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("guess game complemted");
        this.responseObserver.onCompleted();
    }

    private Result guessHint(GuessRequest request) {
        var clientGuess = request.getGuess();
        var result = Result.UNRECOGNIZED;
        if (clientGuess > guessedNumber) {
            result = Result.TOO_HIGH;
        } else if (clientGuess < guessedNumber) {
            result = Result.TOO_LOW;
        } else {
            result = Result.CORRECT;
        }
        return result;
    }
}
