package com.chatty.handler;

import com.chatty.constants.Code;
import com.chatty.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomHttpStatusEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();


        ErrorResponse errorResponse = ErrorResponse.of(Code.INVALID_TOKEN.getErrorCode(), Code.INVALID_TOKEN.getMessage());

        response.setStatus(Code.INVALID_TOKEN.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
