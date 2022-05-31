package ning.controller;

import ning.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {

    @GetMapping(value = "/login")
    public Mono<Result> login() {
        return Mono.just(Result.data(-1, "PLEASE LOGIN", "NO LOGIN"));
    }

}
