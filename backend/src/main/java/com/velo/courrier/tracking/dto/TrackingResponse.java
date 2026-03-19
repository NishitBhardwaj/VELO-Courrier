package com.velo.courrier.tracking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackingResponse {
    private DriverPoint driver;
    private String status;
    private Integer eta;

    @Data
    @Builder
    public static class DriverPoint {
        private double lat;
        private double lng;
    }
}
