package com.nexusforge.grpcplayground.sec08;

import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec07.FlowControlServieGrpc;
import com.nexusforge.grpcplayground.models.sec08.GuessNumberGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessGameTest extends AbstractChannelTest {
    private final GrpcServer server = GrpcServer.create(new GameService());
    private GuessNumberGrpc.GuessNumberStub stub;

    @BeforeAll
    public void setup() {
        this.server.start();
        this.stub = GuessNumberGrpc.newStub(channel);
    }

    @Test
    public void guessGameTest() {
        var responseObserver = new ResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);

        responseObserver.setRequestObserver(requestObserver);

        responseObserver.start();
        responseObserver.await();
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

}
