package com.velo.courrier.pricing;

import com.velo.courrier.common.dto.Address;
import com.velo.courrier.vehicle.VehicleCategory;
import com.velo.courrier.vehicle.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final VehicleCategoryRepository categoryRepository;

    public BigDecimal calculateEstimate(UUID categoryId, Address pickup, Address dropoff) {
        VehicleCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Vehicle category not found for pricing"));
        
        // Mock distance calculation based on Lat/Lon (Haversine formula in reality)
        double distanceKm = mockDistanceAlgorithm(pickup, dropoff);
        
        // Fare = Base + (Distance * rate)
        BigDecimal distanceFare = category.getPerKmRate().multiply(BigDecimal.valueOf(distanceKm));
        BigDecimal estFare = category.getBaseFare().add(distanceFare);
        
        return estFare.setScale(2, RoundingMode.HALF_UP);
    }

    private double mockDistanceAlgorithm(Address pickup, Address dropoff) {
        if (pickup.getLatitude() == null || dropoff.getLatitude() == null) return 10.0;
        
        double latDiff = pickup.getLatitude() - dropoff.getLatitude();
        double lonDiff = pickup.getLongitude() - dropoff.getLongitude();
        double r = Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
        
        // roughly 1 deg = 111 km
        return r * 111.0;
    }
}
