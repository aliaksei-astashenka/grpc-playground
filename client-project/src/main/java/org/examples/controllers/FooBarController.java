package org.examples.controllers;

import org.examples.services.FooBarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.examples.config.LogGrpcInterceptor.TRACE_ID_KEY;

@RestController
public class FooBarController {

    private final FooBarService fooBarService;

    public FooBarController(FooBarService fooBarService) {
        this.fooBarService = fooBarService;
    }

    @GetMapping("foo")
    public String getFoo(@RequestParam String name, @RequestHeader("requestTraceId") String requestTraceId) {
        MDC.put(TRACE_ID_KEY.originalName(), requestTraceId);

        return fooBarService.receiveGreeting(name);
    }
}
