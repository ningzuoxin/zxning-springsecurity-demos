package com.ning.controller;

import com.ning.model.Result;
import com.ning.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @GetMapping(value = "/login")
    public Result login() {
        return Result.data(-1, "PLEASE LOGIN", "NO LOGIN");
    }

    @GetMapping(value = "/logout")
    public Result logout() {
        return Result.data(200, "LOGOUT SUCCESS", "OK");
    }

}
