package com.velo.courrier.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/kpis")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'OPS')")
    public ResponseEntity<?> getOperationalKpis() {
        return ResponseEntity.ok(analyticsService.getOperationalKpis());
    }
    
    // Future Expansion Endpoints
    // @GetMapping("/zones") 
    // @GetMapping("/drivers")
}
