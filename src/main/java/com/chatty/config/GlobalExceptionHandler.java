package com.chatty.config;

import static com.chatty.constants.Code.*;

import com.chatty.dto.ErrorResponseDto;
import com.chatty.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponseDto> handleNormalException(CustomException e){
        return ResponseEntity.status(e.getCode().getHttpStatus()).body(ErrorResponseDto.of(e.getCode()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> validationException(BindException e){
        return ResponseEntity.status(INVALID_PARAMETER.getHttpStatus()).body(ErrorResponseDto.of(INVALID_PARAMETER));
    }

    @ExceptionHandler({JsonProcessingException.class, RestClientException.class, URISyntaxException.class, InvalidKeyException.class, NoSuchAlgorithmException.class, UnsupportedEncodingException.class})
    public ResponseEntity<ErrorResponseDto> sendSmsException(Exception e){
        return ResponseEntity.status(NOT_SEND_SMS.getHttpStatus()).body(ErrorResponseDto.of(NOT_SEND_SMS));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> userException(UsernameNotFoundException e){
        return ResponseEntity.status(NOT_EXIST_USER.getHttpStatus()).body(ErrorResponseDto.of(NOT_EXIST_USER));
    }
}
