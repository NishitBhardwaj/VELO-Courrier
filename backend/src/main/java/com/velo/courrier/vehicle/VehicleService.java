package com.velo.courrier.vehicle;

import com.velo.courrier.driver.DriverProfile;
import com.velo.courrier.driver.DriverProfileRepository;
import com.velo.courrier.vehicle.dto.VehicleRequest;
import com.velo.courrier.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository categoryRepository;
    private final DriverProfileRepository driverRepository;

    @Transactional
    public VehicleResponse registerVehicle(UUID driverId, VehicleRequest request) {
        DriverProfile driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        VehicleCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(driver);
        vehicle.setCategory(category);
        vehicle.setPlateNumber(request.getPlateNumber());
        
        vehicle = vehicleRepository.save(vehicle);

        return mapToResponse(vehicle);
    }

    public List<VehicleResponse> getDriverVehicles(UUID driverId) {
        // In real app, write a custom query in repository
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getDriver() != null && v.getDriver().getUserId().equals(driverId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .plateNumber(vehicle.getPlateNumber())
                .categoryName(vehicle.getCategory() != null ? vehicle.getCategory().getName() : null)
                .driverId(vehicle.getDriver() != null ? vehicle.getDriver().getUserId() : null)
                .build();
    }
}
