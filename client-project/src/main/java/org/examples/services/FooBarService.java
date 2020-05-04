package org.examples.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.examples.lib.HelloRequest;
import org.examples.lib.MyServiceGrpc;
import org.springframework.stereotype.Service;

@Service
public class FooBarService {

    @GrpcClient("my-grpc-server")
    private MyServiceGrpc.MyServiceBlockingStub myServiceStub;

    public String receiveGreeting(String name) {
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        return myServiceStub.sayHello(request).getMessage();
    }
}
