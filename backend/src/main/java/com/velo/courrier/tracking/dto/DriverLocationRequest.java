package com.velo.courrier.tracking.dto;

import lombok.Data;

@Data
public class DriverLocationRequest {
    private double latitude;
    private double longitude;
    private Double heading;
    private Double speed;
    private Double accuracy;
}
