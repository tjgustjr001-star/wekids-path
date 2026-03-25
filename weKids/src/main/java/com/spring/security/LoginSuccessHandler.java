package com.spring.security;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String targetUrl = request.getContextPath() + "/";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if ("ROLE_ADMIN".equals(role)) {
                targetUrl = request.getContextPath() + "/admin";
                break;
            } else if ("ROLE_TEACHER".equals(role)) {
                targetUrl = request.getContextPath() + "/teacher";
                break;
            } else if ("ROLE_PARENT".equals(role)) {
                targetUrl = request.getContextPath() + "/parent";
                break;
            } else if ("ROLE_STUDENT".equals(role)) {
                targetUrl = request.getContextPath() + "/student";
                break;
            }
        }

        response.sendRedirect(targetUrl);
    }
}