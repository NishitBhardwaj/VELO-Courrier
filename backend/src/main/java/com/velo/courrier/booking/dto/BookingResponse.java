package com.velo.courrier.booking.dto;

import com.velo.courrier.common.dto.Address;
import com.velo.courrier.common.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private String id;
    private BookingStatus status;
    private BigDecimal fareEstimate;
    private String serviceType;
    private Address pickupAddress;
    private Address dropoffAddress;
    private String customerId;
    private String driverId;
}
