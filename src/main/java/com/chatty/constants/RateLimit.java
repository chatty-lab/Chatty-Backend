package com.chatty.constants;

import java.time.Duration;

public class RateLimit {
    public static final Long BUCKET_CAPACITY = 10L;
    public static final Long BUCKET_TOKENS = 10L;
    public static final Duration CALLS_IN_SECONDS = Duration.ofSeconds(1);
    public static final Integer REQUEST_COST_IN_TOKENS = 1;
}
