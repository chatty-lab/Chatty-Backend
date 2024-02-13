package com.chatty.handler;

import com.chatty.constants.Code;
import com.chatty.dto.ErrorResponse;
import com.chatty.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        setResponse(request,response);
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        CustomException exception = (CustomException) request.getAttribute("errorCode");
        Code errorCode = exception.getCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getErrorCode(), errorCode.getMessage());
        response.setStatus(Code.INVALID_TOKEN.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
