package org.examples.controllers;

import org.examples.services.FooBarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooBarController {

    private final FooBarService fooBarService;

    public FooBarController(FooBarService fooBarService) {
        this.fooBarService = fooBarService;
    }

    @GetMapping("foo")
    public String getFoo(@RequestParam String name) {
        return fooBarService.receiveGreeting(name);
    }
}
