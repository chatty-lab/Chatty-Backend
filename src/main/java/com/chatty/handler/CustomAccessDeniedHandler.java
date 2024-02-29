package com.chatty.handler;

import com.chatty.constants.Code;
import com.chatty.dto.ErrorResponse;
import com.chatty.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.chatty.constants.Code.*;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HttpStatus httpStatus;

    public CustomAccessDeniedHandler(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(this.httpStatus.value());

        String role = request.getAttribute("role").toString();
        if (role.equals("ANONYMOUS")) {
            ErrorResponse errorResponse =
                    ErrorResponse.of(NOT_AUTHORITY_USER.getErrorCode(), NOT_AUTHORITY_USER.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else if (role.equals("USER")) {
            ErrorResponse errorResponse =
                    ErrorResponse.of(NOT_AUTHORITY.getErrorCode(), NOT_AUTHORITY.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }

    }
}
