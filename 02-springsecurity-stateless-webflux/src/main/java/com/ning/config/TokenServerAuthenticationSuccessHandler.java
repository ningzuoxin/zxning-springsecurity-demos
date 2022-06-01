package com.ning.config;

import cn.hutool.core.util.IdUtil;
import com.ning.util.ServerHttpResponseUtils;
import com.ning.model.Result;
import com.ning.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TokenServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        String token = IdUtil.simpleUUID();
        authenticationRepository.add(token, authentication);

        Result<String> result = Result.data(token, "LOGIN SUCCESS");
        return ServerHttpResponseUtils.print(webFilterExchange.getExchange().getResponse(), result);
    }

}
