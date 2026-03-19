package com.velo.courrier.admin;

import com.velo.courrier.driver.DriverProfile;
import com.velo.courrier.driver.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DriverProfileRepository driverRepository;

    @PutMapping("/drivers/{id}/kyc/approve")
    public ResponseEntity<Void> approveKyc(@PathVariable UUID id) {
        DriverProfile driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
                
        driver.setKycVerified(true);
        driverRepository.save(driver);
        
        return ResponseEntity.ok().build();
    }
}
