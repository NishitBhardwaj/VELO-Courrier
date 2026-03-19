package com.velo.courrier.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MultiStopBookingRequest {

    @NotNull
    private String serviceType;

    @NotEmpty
    @Valid
    private List<StopDto> stops;

    private String notes;
}
