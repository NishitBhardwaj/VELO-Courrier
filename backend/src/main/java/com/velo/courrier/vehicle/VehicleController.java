package com.velo.courrier.vehicle;

import com.velo.courrier.vehicle.dto.VehicleRequest;
import com.velo.courrier.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<VehicleResponse> registerVehicle(
            Authentication authentication,
            @Valid @RequestBody VehicleRequest request
    ) {
        UUID driverId = UUID.fromString(authentication.getName()); // Mocked extraction
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.registerVehicle(driverId, request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<VehicleResponse>> getMyVehicles(Authentication authentication) {
        UUID driverId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(vehicleService.getDriverVehicles(driverId));
    }
}
