package com.testtask.nauka.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.nauka.common.response.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e)
        throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse error = new ErrorResponse(e.getMessage());
        String errorResponse = mapper.writeValueAsString(error);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.getWriter().write(errorResponse);
    }
}
