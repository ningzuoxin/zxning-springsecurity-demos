package com.ning.config;

import com.ning.util.ServerHttpResponseUtils;
import com.ning.model.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TokenServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        Result<String> result = Result.data(-1, exception.getMessage(), "LOGIN FAILED");
        return ServerHttpResponseUtils.print(webFilterExchange.getExchange().getResponse(), result);
    }

}
