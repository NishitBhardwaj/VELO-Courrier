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
    private String pickupAddress;
    private String dropoffAddress;
    private String customerId;
    private String driverId;

    // Sprint 2 Multi-Stop Fields
    private boolean multiStopEnabled;
    private Integer totalStops;
    private Integer currentStopOrder;
    private List<StopDto> stops;
}
