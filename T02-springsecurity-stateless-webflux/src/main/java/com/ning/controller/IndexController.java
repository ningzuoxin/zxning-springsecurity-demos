package com.ning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class IndexController {

    @RequestMapping(value = "/index")
    @PreAuthorize("hasAuthority('index')")
    public Mono<String> index() {
        return Mono.just("index");
    }

    @RequestMapping(value = "/hasAuthority")
    @PreAuthorize("hasAuthority('hasAuthority')")
    public Mono<String> hasAuthority() {
        return Mono.just("hasAuthority");
    }

    @RequestMapping(value = "/hasRole")
    @PreAuthorize("hasRole('hasRole')")
    public Mono<String> hasRole() {
        return Mono.just("hasRole");
    }

    @RequestMapping(value = "/home")
    @PreAuthorize("hasRole('home')")
    public Mono<String> home() {
        return Mono.just("home");
    }

}
