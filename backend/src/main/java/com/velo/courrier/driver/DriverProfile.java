package com.velo.courrier.driver;

import com.velo.courrier.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "driver_profiles")
@Getter
@Setter
public class DriverProfile {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "is_kyc_verified")
    private boolean isKycVerified = false;

    @Column(name = "current_status", length = 20)
    private String currentStatus = "OFFLINE"; // ONLINE, OFFLINE, BUSY
}
