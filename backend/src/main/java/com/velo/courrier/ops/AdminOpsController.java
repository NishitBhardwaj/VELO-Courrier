package com.velo.courrier.ops;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/ops")
@RequiredArgsConstructor
public class AdminOpsController {

    private final AdminOpsService adminOpsService;

    @GetMapping("/map")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getGlobalLiveMap() {
        return ResponseEntity.ok(adminOpsService.getGlobalLiveMap());
    }
}
