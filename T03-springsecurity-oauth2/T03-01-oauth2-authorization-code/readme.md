```
1、请求地址 http://127.0.0.1:8080，抛出 AccessDeniedException。
2、因为是匿名用户，会跳转登录页面，默认的登录页面地址是 http://127.0.0.1:8080/oauth2/authorization/login-client。
3、默认的登录地址 http://127.0.0.1:8080/login，DefaultLoginPageGeneratingFilter 会创建默认的登录页面（会判断 oauth2Login 为 true）。

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

```
