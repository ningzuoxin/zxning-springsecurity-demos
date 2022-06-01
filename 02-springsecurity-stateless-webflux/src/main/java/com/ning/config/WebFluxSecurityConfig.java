package com.ning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    @Autowired
    private TokenServerAuthenticationSuccessHandler tokenServerAuthenticationSuccessHandler;
    @Autowired
    private TokenServerAuthenticationFailureHandler tokenServerAuthenticationFailureHandler;
    @Autowired
    private TokenWebFilter tokenWebFilter;

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder().username("admin")
                .passwordEncoder(passwordEncoder()::encode)
                .password("123456")
                .authorities("ROLE_USER")
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(s -> s.disable())
                .formLogin(s -> s
                        .loginPage("/login")
                        .authenticationSuccessHandler(tokenServerAuthenticationSuccessHandler)
                        .authenticationFailureHandler(tokenServerAuthenticationFailureHandler))
                .authorizeExchange(s -> s.pathMatchers("/login").permitAll().anyExchange().authenticated())
                .addFilterBefore(tokenWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
