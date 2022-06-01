一、webflux集成spring security会经过的一些过滤器。
```
org.springframework.security.config.web.server.ServerHttpSecurity$ServerWebExchangeReactorContextWebFilter
org.springframework.security.web.server.header.HttpHeaderWriterWebFilter
org.springframework.security.web.server.context.ReactorContextWebFilter
org.springframework.security.web.server.authentication.AuthenticationWebFilter
org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter
org.springframework.security.web.server.savedrequest.ServerRequestCacheWebFilter
org.springframework.security.web.server.authentication.logout.LogoutWebFilter
org.springframework.security.web.server.authorization.ExceptionTranslationWebFilter
org.springframework.security.web.server.authorization.AuthorizationWebFilter
```

二、webflux使用spring security流程分析。
```
(一)在 ReactorContextWebFilter 中，会始终执行 withSecurityContext 方法，就会从 ServerSecurityContextRepository 去 load Mono<SecurityContext>，系统有两个默认的实现类分别是 
NoOpServerSecurityContextRepository（均返回空） 和 WebSessionServerSecurityContextRepository（基于session）。
(二)在 AuthenticationWebFilter 中，如果通过了认证，就会执行 onAuthenticationSuccess 方法，这个时候会创建一个 SecurityContext，然后通过 ServerSecurityContextRepository 的 save 方法进行保存。
```
