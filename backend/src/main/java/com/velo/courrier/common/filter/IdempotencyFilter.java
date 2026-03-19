package com.velo.courrier.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
            
        // Only apply idempotency to Booking POST requests
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().contains("/api/v1/bookings")) {
            
            String idempotencyKey = request.getHeader("Idempotency-Key");
            
            if (idempotencyKey != null && !idempotencyKey.isBlank()) {
                String redisKey = "idempotency:" + idempotencyKey;
                
                // Try to set the key. If it exists, it means this request was already processed.
                Boolean isNew = redisTemplate.opsForValue().setIfAbsent(redisKey, "PROCESSING", Duration.ofHours(1));
                
                if (Boolean.FALSE.equals(isNew)) {
                    log.warn("Duplicate request detected for Idempotency-Key: {}", idempotencyKey);
                    response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                    response.getWriter().write("{\"error\": \"Duplicate request. Please do not double-click.\"}");
                    return;
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
