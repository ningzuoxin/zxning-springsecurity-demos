package com.ning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    @RequestMapping(value = "/")
    public Mono<String> root() {
        return Mono.just("hello root");
    }

    @RequestMapping(value = "/test")
    public Mono<String> test() {
        return Mono.just("hello test");
    }

}
