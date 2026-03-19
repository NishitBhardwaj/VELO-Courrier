package com.velo.courrier.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VehicleResponse {
    private UUID id;
    private String plateNumber;
    private String categoryName;
    private UUID driverId;
}
