package com.chatty.common;

public enum CommonResponse {
    SUCCESS(0,"Success"), FAIL(-1,"Fail");

    private int code;
    private String message;

    CommonResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
