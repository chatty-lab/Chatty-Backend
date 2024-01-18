package com.chatty.dto.ratelimit;

import com.chatty.constants.Code;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;

public class RateLimitResponse {

    public static void successResponse(HttpServletResponse response, long remainingTokens, Long bucketCapacity, Duration callsInSecond){
        response.setHeader("X-RateLimit-Remaining", Long.toString(remainingTokens));
        response.setHeader("X-RateLimit-Limit", bucketCapacity + ";w=" + callsInSecond.getSeconds());
    }

    public static void errorResponse(HttpServletResponse response, Long bucketCapacity, Duration callsInSeconds, float waitForRefill){
        response.setHeader("X-RateLimit-RetryAfter", Float.toString(waitForRefill));
        response.setHeader("X-RateLimit-Limit", bucketCapacity + ";w=" + callsInSeconds.getSeconds());

        response.setStatus(Code.TOO_MANY_REQUESTS.getHttpStatus().value());
        response.getWriter().write();
    }
}
