package com.velo.courrier.booking.dto;

import com.velo.courrier.common.dto.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingRequest {

    @NotBlank(message = "Service type is required")
    private String serviceType;
    
    @NotNull(message = "Vehicle category ID required to estimate fare")
    private UUID vehicleCategoryId;

    @NotNull(message = "Pickup address is required")
    private Address pickupAddress;

    @NotNull(message = "Dropoff address is required")
    private Address dropoffAddress;

    private LocalDateTime scheduledTime;
}
