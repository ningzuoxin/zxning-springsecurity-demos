package com.ning.config;

import com.ning.model.Result;
import com.ning.util.ServerHttpResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        Result<String> result = Result.data(403, denied.getMessage(), "ACCESS DENIED");
        return ServerHttpResponseUtils.print(exchange.getResponse(), result);
    }

}
