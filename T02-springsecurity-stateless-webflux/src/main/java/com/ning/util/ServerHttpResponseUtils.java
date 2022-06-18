package com.ning.util;

import cn.hutool.json.JSONUtil;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ServerHttpResponseUtils {

    public static Mono<Void> print(ServerHttpResponse response, Object data) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONUtil.toJsonStr(data).getBytes(StandardCharsets.UTF_8))));
    }

}
