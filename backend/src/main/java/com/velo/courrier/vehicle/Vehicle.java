package com.velo.courrier.vehicle;

import com.velo.courrier.common.entity.BaseEntity;
import com.velo.courrier.driver.DriverProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private VehicleCategory category;

    @Column(name = "plate_number", unique = true, length = 20)
    private String plateNumber;
}
