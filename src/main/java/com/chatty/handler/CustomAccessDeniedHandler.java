package com.chatty.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HttpStatus httpStatus;

    public CustomAccessDeniedHandler(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(this.httpStatus.value());
    }
}
