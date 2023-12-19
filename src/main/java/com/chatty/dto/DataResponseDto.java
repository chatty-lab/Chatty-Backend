package com.chatty.dto;

import com.chatty.constants.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    private final T data;

    public DataResponseDto(T data) {
        super(ErrorCode.OK.getHttpStatus().value(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> DataResponseDto<T> of(T data) {
        return new DataResponseDto<>(data);
    }
}
