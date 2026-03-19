package com.velo.courrier.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookIdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Guard Stripe Webhooks using Redis SETNX mapping the exact Stripe 'evt_' ID.
     */
    public boolean lockWebhookEvent(String stripeEventId) {
        String redisKey = "stripe_webhook:" + stripeEventId;
        
        // Lock lifetime is 24 hours. If Stripe retries within 24 hours, it gets rejected.
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(redisKey, "PROCESSED", Duration.ofHours(24));
        
        if (Boolean.FALSE.equals(isNew)) {
            log.warn("🚨 Stopped duplicate webhook execution for Stripe Event: {}", stripeEventId);
            return false;
        }
        return true;
    }
}
