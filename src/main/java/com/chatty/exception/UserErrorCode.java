package com.chatty.exception;

import com.chatty.dto.ErrorReason;
import com.chatty.swagger.BaseErrorCode;
import com.chatty.swagger.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    @ExplainError("회원가입시 이미 회원가입을 한 유저일 때, 발생하는 오류")
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재 하는 유저 입니다.","002");

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().reason(reason).code(code).status(status).build();
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getReason();
    }
}
