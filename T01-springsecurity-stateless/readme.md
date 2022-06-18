1、01-springsecurity-stateless 是基于 spring security 实现自定义 token 以满足前后端分离，完全符合 restful 风格的权限保护需求。
2、认证登录逻辑仍旧是使用的 UsernamePasswordAuthenticationFilter 的代码逻辑。
3、认证成功后，经过 TokenAuthenticationSuccessHandler 返回 token json 给前端。
4、认证失败后，经过 TokenAuthenticationFailureHandler 返回错误提示给前端。
5、自定义 TokenAuthenticationFilter ，通过 request 中 header 的 token 取得 authentication，并放入 SecurityContextHolder。
6、注意，要禁用 csrf （csrf().disable()），不然会提示 403 。
