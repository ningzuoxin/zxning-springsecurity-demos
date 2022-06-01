package com.ning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;
    @Autowired
    private TokenAuthenticationFailureHandler tokenAuthenticationFailureHandler;
    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;
    @Autowired
    private TokenLogoutSuccessHandler tokenLogoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 权限配置
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("index"));
        authorities.add(new SimpleGrantedAuthority("hasAuthority"));
        authorities.add(new SimpleGrantedAuthority("ROLE_hasRole"));

        // 认证信息
        UserDetails userDetails = User.builder().username("admin").password(passwordEncoder().encode("123456")).authorities(authorities).build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 放行 /login 请求，其他请求必须经过认证
        http.authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated()
                // 配置退出成功处理器
                .and().logout().logoutSuccessHandler(tokenLogoutSuccessHandler)
                // 配置无访问权限处理器
                .and().exceptionHandling().accessDeniedHandler(new TokenAccessDeniedHandler())
                // 指定登录请求url
                .and().formLogin().loginPage("/login")
                // 配置认证成功处理器
                .successHandler(tokenAuthenticationSuccessHandler)
                // 配置认证失败处理器
                .failureHandler(tokenAuthenticationFailureHandler)
                // 将 session 管理策略设置为 STATELESS （无状态）
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 禁用防止 csrf
                .and().csrf().disable()
                // 在 UsernamePasswordAuthenticationFilter 过滤器之前，添加自定义的 token 认证过滤器
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
