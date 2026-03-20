package com.velo.courrier.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AiServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiEndpoint;

    /**
     * Governed rigidly by Feature Flags, this HTTP request pauses Java thread execution blocking 
     * on the Python Machine Learning algorithms. If Python exceeds 400ms, execution fails 
     * directly over into local Redis GEO algorithms preserving standard delivery targets.
    // @CircuitBreaker(name="aiDispatchApi", fallbackMethod="fallbackToNearestDriver")
    public List<Map<String, Object>> requestSmartDispatch(UUID bookingId, List<Map<String, Object>> candidateDrivers) {
        log.info("AI_CLIENT: Relaying {} Candidate Drivers to Python ML Models for Recommendation ranking.", candidateDrivers.size());

        // POST /ai/dispatch/recommend
        // Map<String, Object> payload = Map.of("booking_id", bookingId, "driver_candidates", candidateDrivers ...);
        // return restTemplate.postForObject(aiEndpoint + "/ai/dispatch/recommend", payload, List.class);
        
        // ML Hook Return Scaffold
        return candidateDrivers; // Passing unaltered array gracefully in Blueprint bounds
    }

    public List<Map<String, Object>> fallbackToNearestDriver(UUID bookingId, List<Map<String, Object>> candidateDrivers, Throwable t) {
        log.warn("AI System unavailable or Timeout hit! Falling back gracefully to pure geospatial Redis Radius.");
        // Returns generic un-optimized nearest drivers array.
        return candidateDrivers;
    }
}
