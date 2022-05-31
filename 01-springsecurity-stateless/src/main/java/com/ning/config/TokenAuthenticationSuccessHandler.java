package com.ning.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.ning.model.Result;
import com.ning.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = IdUtil.simpleUUID();
        authenticationRepository.add(token, authentication);

        Result<String> result = Result.data(token, "LOGIN SUCCESS");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.toJsonStr(result));
        response.getWriter().flush();
    }
}
