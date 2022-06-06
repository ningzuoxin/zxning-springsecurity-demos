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

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("index"));
        authorities.add(new SimpleGrantedAuthority("hasAuthority"));
        authorities.add(new SimpleGrantedAuthority("ROLE_hasRole"));

        UserDetails userDetails = User.builder().username("admin")
                .passwordEncoder(passwordEncoder()::encode)
                .password("123456")
                .authorities(authorities)
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(s -> s.disable())
                .securityContextRepository(tokenServerSecurityContextRepository)
                .formLogin(s -> s
                        .loginPage("/login")
                        .authenticationSuccessHandler(tokenServerAuthenticationSuccessHandler)
                        .authenticationFailureHandler(tokenServerAuthenticationFailureHandler)
                )
                .logout(s -> s.logoutSuccessHandler(new TokenServerLogoutSuccessHandler()))
                .authorizeExchange(s -> s.pathMatchers("/login").permitAll().anyExchange().authenticated())
                .exceptionHandling().accessDeniedHandler(new TokenServerAccessDeniedHandler());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
