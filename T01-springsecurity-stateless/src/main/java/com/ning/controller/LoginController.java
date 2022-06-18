package com.ning.controller;

import com.ning.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(value = "/login")
    public Result login() {
        return Result.data(-1, "PLEASE LOGIN", "NO LOGIN");
    }

}
