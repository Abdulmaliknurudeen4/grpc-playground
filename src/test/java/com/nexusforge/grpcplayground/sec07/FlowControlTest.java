package com.nexusforge.grpcplayground.sec07;

import com.nexusforge.grpcplayground.common.AbstractChannelTest;
import com.nexusforge.grpcplayground.common.GrpcServer;
import com.nexusforge.grpcplayground.models.sec07.FlowControlServieGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlowControlTest extends AbstractChannelTest {
    private final GrpcServer server = GrpcServer.create(new FlowControlService());
    private FlowControlServieGrpc.FlowControlServieStub stub;

    @BeforeAll
    public void setup() {
        this.server.start();
        this.stub = FlowControlServieGrpc.newStub(channel);
    }

    @Test
    public void flowControlDemo(){
        var responseObserver = new ResponseHandler();
        var requestObserver = this.stub.getMessages(responseObserver);

        responseObserver.setRequestObserver(requestObserver);

        responseObserver.start();
        responseObserver.await();
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

}
