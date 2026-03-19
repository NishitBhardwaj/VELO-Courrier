package com.velo.courrier.driver.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DriverStatusUpdateRequest {
    
    @Pattern(regexp = "^(ONLINE|OFFLINE|BUSY)$")
    private String status;
}
