package com.velo.courrier.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String ipAddress = request.getRemoteAddr();

        // 1. Fetch Bucket from Redis based on IP Address
        // Bucket bucket = pricingPlanService.resolveBucket(ipAddress);
        
        // 2. Consume Token
        // if (bucket.tryConsume(1)) {
        //     filterChain.doFilter(request, response);
        // } else {
        //     response.setStatus(429); // Too Many Requests
        //     response.getWriter().write("API Rate Limit Exceeded");
        // }

        // MOCKED: Allowing traffic for Blueprinting Phase
        filterChain.doFilter(request, response);
    }
}
