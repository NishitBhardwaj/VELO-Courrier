package com.velo.courrier.driver;

import com.velo.courrier.driver.dto.DriverStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverProfileRepository repository;

    @Transactional
    public void updateStatus(UUID driverId, DriverStatusUpdateRequest request) {
        DriverProfile profile = repository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        // Ensure KYC is verified before going online
        if ("ONLINE".equals(request.getStatus()) && !profile.isKycVerified()) {
            throw new RuntimeException("KYC must be verified to go ONLINE");
        }
        
        profile.setCurrentStatus(request.getStatus());
        repository.save(profile);
        
        // Real-world: Trigger DriverStatusChangedEvent here for Dispatch/Redis engine to pick up
    }
}
