package com.chatty.config;

import static com.chatty.constants.Code.*;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.ErrorResponseDto;
import com.chatty.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponseDto> handleNormalException(CustomException e){
        return ResponseEntity.status(e.getCode().getHttpStatus()).body(ErrorResponseDto.of(e.getCode()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> validationException(BindException e){
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<ErrorResponseDto> validationException(BindException e){
//        return ResponseEntity.status(INVALID_PARAMETER.getHttpStatus())
//                .body(ErrorResponseDto.of(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
//    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<ErrorResponseDto> validationException(BindException e){
//        return ResponseEntity.status(INVALID_PARAMETER.getHttpStatus()).body(ErrorResponseDto.of(INVALID_PARAMETER));
//    }

    @ExceptionHandler({JsonProcessingException.class, RestClientException.class, URISyntaxException.class, InvalidKeyException.class, NoSuchAlgorithmException.class, UnsupportedEncodingException.class})
    public ResponseEntity<ErrorResponseDto> sendSmsException(Exception e){
        //TODO: LocalDate에 값을 어떻게 검증할 것인지? 이상하게 넣으면 Parsing 예외 발생한다.
        e.printStackTrace();
        return ResponseEntity.status(NOT_SEND_SMS.getHttpStatus()).body(ErrorResponseDto.of(e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> userException(UsernameNotFoundException e){
        return ResponseEntity.status(NOT_EXIST_USER.getHttpStatus()).body(ErrorResponseDto.of(NOT_EXIST_USER));
    }
}
