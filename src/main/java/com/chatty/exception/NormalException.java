package com.chatty.exception;

import com.chatty.constants.ErrorCode;
import lombok.Getter;

@Getter
public class NormalException extends RuntimeException{

    private final ErrorCode errorCode;

    public NormalException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
