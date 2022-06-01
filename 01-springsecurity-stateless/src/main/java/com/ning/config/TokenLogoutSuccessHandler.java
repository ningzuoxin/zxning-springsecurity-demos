package com.ning.config;

import cn.hutool.core.util.StrUtil;
import com.ning.model.Result;
import com.ning.repository.AuthenticationRepository;
import com.ning.util.HttpServletResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Result<String> result = null;

        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            result = Result.data(-1, "NEED TOKEN", "ERROR");
        } else {
            authenticationRepository.delete(token);
            result = Result.data(200, "LOGOUT SUCCESS", "OK");
        }

        HttpServletResponseUtils.print(response, result);
    }

}
