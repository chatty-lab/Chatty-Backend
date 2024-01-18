package com.chatty.interceptor;

import static com.chatty.constants.RateLimit.*;

import com.chatty.dto.ratelimit.RateLimitResponse;
import com.chatty.utils.rateLimit.RateLimitRefillChecker;
import com.chatty.utils.rateLimit.RateLimitUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j // Lombok 어노테이션으로 로거 객체를 자동 생성
@Component // 스프링 빈으로 등록하기 위한 어노테이션
@RequiredArgsConstructor // Lombok 어노테이션으로 final 필드나 @NonNull 필드에 대한 생성자를 자동 생성
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>(); // 클라이언트 IP별 버킷을 저장하는 캐시

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = RateLimitUtil.getClientIp(request); // 요청한 클라이언트의 IP 주소를 가져옴

        // 해당 클라이언트 IP에 대한 버킷이 없으면 새로 생성
        Bucket bucket = cache.computeIfAbsent(clientIp, key -> makeBucket());
        // 버킷에서 토큰을 소비하고 남은 토큰 정보를 가져옴
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(REQUEST_COST_IN_TOKENS);

        // 속도 제한을 초과했는지 확인
        if(isRateLimitExceeded(request, response, clientIp, consumptionProbe)){
            return false; // 속도 제한을 초과한 경우 요청 처리 중단
        }
        return true; // 속도 제한을 초과하지 않은 경우 요청 처리 계속
    }

    // 속도 제한 초과 여부를 확인하고, 응답을 설정하는 메서드
    private boolean isRateLimitExceeded(HttpServletRequest request, HttpServletResponse response, String clientIp, ConsumptionProbe consumptionProbe){
        if(!consumptionProbe.isConsumed()){
            // 토큰이 소진되었을 경우 재충전까지 대기해야 하는 시간 계산
            float waitForRefill = RateLimitRefillChecker.getRoundedSecondsToWaitForRefill(consumptionProbe);

            // 클라이언트에게 속도 제한 초과 응답을 보냄
            RateLimitResponse.errorResponse(
                    response, BUCKET_CAPACITY, CALLS_IN_SECONDS, waitForRefill);

            // 로그 기록
            log.warn(
                    "rate limit exceeded for client IP :{} Refill in {} seconds Request " + "details: method = {} URI = {}",
                    clientIp, waitForRefill, request.getMethod(), request.getRequestURI()
            );

            return true; // 속도 제한 초과
        }

        // 정상적으로 토큰을 소비한 경우 클라이언트에게 성공 응답을 보냄
        RateLimitResponse.successResponse(
                response, consumptionProbe.getRemainingTokens(), BUCKET_CAPACITY, CALLS_IN_SECONDS
        );

        // 남은 토큰 수를 로그에 기록
        log.info("remaining token : {}", consumptionProbe.getRemainingTokens());
        return false; // 속도 제한을 초과하지 않음
    }

    // 버킷을 생성하는 메서드
    private Bucket makeBucket(){
        return Bucket.builder()
                .addLimit(Bandwidth.classic(
                        BUCKET_CAPACITY, Refill.intervally(BUCKET_TOKENS, CALLS_IN_SECONDS)
                ))
                .build();
    }
}
