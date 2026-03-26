package com.spring.security;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String loginId = request.getParameter("login_id");
        String encodedLoginId = loginId == null ? "" : URLEncoder.encode(loginId, "UTF-8");

        response.sendRedirect(
            request.getContextPath() + "/auth/login?error=1&login_id=" + encodedLoginId
        );
    }
}