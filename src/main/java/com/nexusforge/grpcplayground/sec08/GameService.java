package com.nexusforge.grpcplayground.sec08;

import com.nexusforge.grpcplayground.models.sec08.GuessNumberGrpc;
import com.nexusforge.grpcplayground.models.sec08.GuessRequest;
import com.nexusforge.grpcplayground.models.sec08.GuessResponse;
import com.nexusforge.grpcplayground.sec08.handler.GuessRequestHandler;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class GameService extends GuessNumberGrpc.GuessNumberImplBase {


    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessRequestHandler(responseObserver);
    }
}
