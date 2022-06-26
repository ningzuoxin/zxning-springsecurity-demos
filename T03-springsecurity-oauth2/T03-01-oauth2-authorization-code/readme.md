```
1、请求地址 http://127.0.0.1:8080，抛出 AccessDeniedException。
2、因为是匿名用户，会重定向至登录地址，默认的登录地址是 http://127.0.0.1:8080/oauth2/authorization/login-client。
3、若直接访问 http://127.0.0.1:8080/login，DefaultLoginPageGeneratingFilter 会创建默认的登录页面（会判断 oauth2Login 为 true），登录地址也是 http://127.0.0.1:8080/oauth2/authorization/login-client。
4、请求被重定向至 http://127.0.0.1:8080/oauth2/authorization/login-client 后，会被 OAuth2AuthorizationRequestRedirectFilter 拦截，会创建 OAuth2AuthorizationRequest。
5、之后，会将 OAuth2AuthorizationRequest 重定向至认证地址 http://localhost:9000/oauth2/authorize，开始进入"认证服务器"端流程。
6、认证请求 http://localhost:9000/oauth2/authorize，会被 OAuth2AuthorizationEndpointFilter 拦截。
7、由于此时尚未认证，认证请求 http://localhost:9000/oauth2/authorize，也会抛出 AccessDeniedException，并重定向至登录地址 http://localhost:9000/login。
8、使用正确的账号密码认证后，请求再次被 OAuth2AuthorizationRequestRedirectFilter 拦截，此时会提示"需要授权确认"，会显示默认的"授权确认"页面。
9、选择确认授权并提交，请求再次被 OAuth2AuthorizationRequestRedirectFilter 拦截。一切正常，会执行 onAuthenticationSuccess 方法。
10、在 onAuthenticationSuccess 方法中，会创建 code，并重定向至如下请求：
http://127.0.0.1:8080/login/oauth2/code/login-client?code=EbfJkvjhKhJUM2ln8vOX-cG5RnBADxRCPeJkbaoerFuRKxvStJ6W-jHMWltAfizvDGbgMHBVKaT0IaxFVqNA3tq3I9Zv0clsRLN0Er2bYQqkPqZJCQFav2GxoNXwvd2T&state=IxAOkTU91kQ9XDY3iR3CKYeKt8MiBngE0ZaYObJb57A%3D
11、返回含有 code 的请求会被 OAuth2LoginAuthenticationFilter 拦截，会进入 attemptAuthentication 方法，然后进入到 OidcAuthorizationCodeAuthenticationProvider 中的 authenticate 方法。
12、在 OidcAuthorizationCodeAuthenticationProvider 中会执行 this.getResponse(authorizationCodeAuthentication); 方法，会交由 DefaultAuthorizationCodeTokenResponseClient 用 code 去获取 token。
13、本质上是由 RestTemplate 发送 HTTP 请求，获取 token，请求地址是 http://localhost:9000/oauth2/token，会携带参数 code 及 redirect_uri。
14、请求 http://localhost:9000/oauth2/token 会被 OAuth2TokenEndpointFilter 拦截，会创建出 OAuth2AccessTokenAuthenticationToken，并执行 onAuthenticationSuccess 方法。
15、成功拿到 token 后，步骤 13 会继续往下执行，
16、NimbusJwkSetEndpointFilter 会处理 /oauth2/jwks 请求。

# Ajax 请求的http头信息特点 x-requested-with
https://www.iteye.com/blog/rd-030-2316105

org.springframework.security.web.session.DisableEncodeUrlFilter
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
org.springframework.security.web.context.SecurityContextPersistenceFilter
org.springframework.security.web.header.HeaderWriterFilter
org.springframework.security.web.csrf.CsrfFilter 
org.springframework.security.web.authentication.logout.LogoutFilter
org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter
org.springframework.security.web.savedrequest.RequestCacheAwareFilter
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter 
org.springframework.security.web.authentication.AnonymousAuthenticationFilter 
org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter 
org.springframework.security.web.session.SessionManagementFilter 
org.springframework.security.web.access.ExceptionTranslationFilter 
org.springframework.security.web.access.intercept.FilterSecurityInterceptor

Q1：为什么在浏览器请求 http://127.0.0.1:8080 会跳转到 http://127.0.0.1:9000/login，而在 postman 请求就直接返回 401呢？
因为在 postman 中，默认 Accept 是 */*，而在 MediaTypeRequestMatcher 中，*/* 是被忽略掉的。

较关键的过滤器：
org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter

```
