package com.chatty.config;

import com.chatty.constants.Code;
import com.chatty.dto.error.ErrorResponseDto;
import com.chatty.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponseDto> handleNormalException(CustomException e){
        return ResponseEntity.status(e.getCode().getHttpStatus()).body(ErrorResponseDto.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> validationException(MethodArgumentNotValidException e){
        return ResponseEntity.status(Code.INVALID_PARAMETER.getHttpStatus()).body(ErrorResponseDto.of(Code.INVALID_PARAMETER.getMessage()));
    }

    @ExceptionHandler({JsonProcessingException.class, RestClientException.class, URISyntaxException.class, InvalidKeyException.class, NoSuchAlgorithmException.class, UnsupportedEncodingException.class})
    public ResponseEntity sendSmsException(Exception e){
        return ResponseEntity.status(Code.NOT_SEND_SMS.getHttpStatus()).body(ErrorResponseDto.of(Code.NOT_SEND_SMS.getMessage()));
    }
}
