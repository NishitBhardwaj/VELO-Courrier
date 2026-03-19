package com.velo.courrier.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StopDto {
    
    @NotNull
    private Integer order;

    @NotBlank
    private String type; // PICKUP or DROP_OFF

    @NotBlank
    private String contactName;

    @NotBlank
    private String contactPhone;

    @NotNull
    private String addressJson; // Should contain coords
}
