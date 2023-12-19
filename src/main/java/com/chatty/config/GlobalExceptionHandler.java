package com.chatty.config;

import com.chatty.dto.ErrorResponseDto;
import com.chatty.exception.NormalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NormalException.class})
    protected ErrorResponseDto handleNormalException(NormalException e){
        return ErrorResponseDto.of(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationException(MethodArgumentNotValidException e){
        return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
    }
}
