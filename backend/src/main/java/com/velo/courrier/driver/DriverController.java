package com.velo.courrier.driver;

import com.velo.courrier.driver.dto.DriverStatusUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PutMapping("/me/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Void> updateStatus(
            Authentication authentication,
            @Valid @RequestBody DriverStatusUpdateRequest request
    ) {
        // Assume CustomUserDetails returns UUID string or parse from name
        // For brevity, fetching UUID from a hypothetical custom extraction
        // In real app: UUID driverId = ((AppUser) authentication.getPrincipal()).getId();
        UUID driverId = UUID.fromString(authentication.getName()); // Mocked mapping
        
        driverService.updateStatus(driverId, request);
        return ResponseEntity.ok().build();
    }
}
