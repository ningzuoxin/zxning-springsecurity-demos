package ning.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import ning.model.Result;
import ning.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class TokenServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String token = IdUtil.simpleUUID();
        authenticationRepository.add(token, authentication);

        Result<String> result = Result.data(token, "LOGIN SUCCESS");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8))));
    }

}
