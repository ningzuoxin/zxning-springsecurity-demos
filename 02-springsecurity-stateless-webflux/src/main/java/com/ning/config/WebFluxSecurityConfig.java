package com.ning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfig {

    @Autowired
    private TokenServerAuthenticationSuccessHandler tokenServerAuthenticationSuccessHandler;
    @Autowired
    private TokenServerAuthenticationFailureHandler tokenServerAuthenticationFailureHandler;
    @Autowired
    private TokenServerSecurityContextRepository tokenServerSecurityContextRepository;
    @Autowired
    private TokenServerLogoutSuccessHandler tokenServerLogoutSuccessHandler;

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        // 权限配置
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("index"));
        authorities.add(new SimpleGrantedAuthority("hasAuthority"));
        authorities.add(new SimpleGrantedAuthority("ROLE_hasRole"));

        // 认证信息
        UserDetails userDetails = User.builder().username("admin")
                .passwordEncoder(passwordEncoder()::encode)
                .password("123456")
                .authorities(authorities)
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // 禁用防止 csrf
        http.csrf(s -> s.disable())
                // 自定义 ServerSecurityContextRepository
                .securityContextRepository(tokenServerSecurityContextRepository)
                .formLogin(s -> s
                        // 指定登录请求url
                        .loginPage("/login")
                        // 配置认证成功处理器
                        .authenticationSuccessHandler(tokenServerAuthenticationSuccessHandler)
                        // 配置认证失败处理器
                        .authenticationFailureHandler(tokenServerAuthenticationFailureHandler)
                )
                // 配置退出成功处理器
                .logout(s -> s.logoutSuccessHandler(tokenServerLogoutSuccessHandler))
                // 放行 /login 请求，其他请求必须经过认证
                .authorizeExchange(s -> s.pathMatchers("/login").permitAll().anyExchange().authenticated())
                // 配置无访问权限处理器
                .exceptionHandling().accessDeniedHandler(new TokenServerAccessDeniedHandler());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
