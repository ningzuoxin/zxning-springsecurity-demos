package com.ning.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ning.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenServerSecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StrUtil.isNotEmpty(token)) {
            Authentication authentication = authenticationRepository.get(token);
            if (ObjectUtil.isNotEmpty(authentication)) {
                SecurityContextImpl securityContext = new SecurityContextImpl();
                securityContext.setAuthentication(authentication);
                return Mono.just(securityContext);
            }
        }
        return Mono.empty();
    }
}
