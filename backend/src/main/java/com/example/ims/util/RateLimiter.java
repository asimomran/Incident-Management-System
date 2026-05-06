package com.example.ims.util;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {
    private final int MAX_REQUESTS_PER_SECOND = 20;
    private AtomicInteger count = new AtomicInteger(0);
    private volatile long windowStart = System.currentTimeMillis();

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        if (now - windowStart >= 1000) {
            windowStart = now;
            count.set(0);
        }
        return count.incrementAndGet() <= MAX_REQUESTS_PER_SECOND;
    }
}
