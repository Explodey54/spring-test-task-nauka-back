package com.testtask.nauka.security;

import com.testtask.nauka.api.auth.data.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CookieAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    public CookieAuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String username = ((UserPrincipal) authentication.getPrincipal()).getUsername();
        String token = jwtProvider.generateToken(username);
        Cookie tokenCookie = new Cookie("jwt_token", token);
        tokenCookie.setMaxAge(60 * 60 * 24);
        httpServletResponse.addCookie(tokenCookie);
        httpServletResponse.sendRedirect("/admin");
    }
}
