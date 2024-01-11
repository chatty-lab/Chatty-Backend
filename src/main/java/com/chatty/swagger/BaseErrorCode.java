package com.chatty.swagger;

import com.chatty.dto.ErrorReason;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;
}
