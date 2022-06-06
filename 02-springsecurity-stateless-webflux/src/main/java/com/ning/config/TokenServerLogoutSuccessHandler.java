package com.ning.config;

import cn.hutool.core.util.StrUtil;
import com.ning.model.Result;
import com.ning.repository.AuthenticationRepository;
import com.ning.util.ServerHttpResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TokenServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        String token = exchange.getExchange().getRequest().getHeaders().getFirst("token");
        if (StrUtil.isNotEmpty(token)) {
            authenticationRepository.delete(token);
        }

        Result<String> result = Result.data(200, "LOGOUT SUCCESS", "OK");
        return ServerHttpResponseUtils.print(exchange.getExchange().getResponse(), result);
    }

}
