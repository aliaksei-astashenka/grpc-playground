package org.examples.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.examples.lib.HelloReply;
import org.examples.lib.HelloRequest;
import org.examples.lib.MyServiceGrpc;

import java.net.InetAddress;
import java.net.UnknownHostException;

@GrpcService
public class MyServiceImpl extends MyServiceGrpc.MyServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName() + ", My address is ==> " + getAddress())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private String getAddress() {
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            return "Unknown:(";
        }
    }
}