package com.chatty.dto;

import lombok.Getter;

@Getter
public class DataResponseDto<T> {

    private final T data;

    public DataResponseDto(T data) {
        this.data = data;
    }

    public static <T> DataResponseDto<T> of(T data){
        return new DataResponseDto<>(data);
    }
}
