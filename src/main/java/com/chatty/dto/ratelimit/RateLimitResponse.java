package com.chatty.dto.ratelimit;

import static com.chatty.constants.Code.TOO_MANY_REQUESTS;

import com.chatty.constants.Code;
import com.chatty.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import org.springframework.http.MediaType;

public class RateLimitResponse {

    public static void successResponse(HttpServletResponse response, long remainingTokens, Long bucketCapacity, Duration callsInSecond){
        response.setHeader("X-RateLimit-Remaining", Long.toString(remainingTokens));
        response.setHeader("X-RateLimit-Limit", bucketCapacity + ";w=" + callsInSecond.getSeconds());
    }

    public static void errorResponse(HttpServletResponse response, Long bucketCapacity, Duration callsInSeconds, float waitForRefill)
            throws IOException {
        response.setHeader("X-RateLimit-RetryAfter", Float.toString(waitForRefill));
        response.setHeader("X-RateLimit-Limit", bucketCapacity + ";w=" + callsInSeconds.getSeconds());

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(TOO_MANY_REQUESTS.getErrorCode(), TOO_MANY_REQUESTS.getMessage());
        response.setStatus(TOO_MANY_REQUESTS.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
