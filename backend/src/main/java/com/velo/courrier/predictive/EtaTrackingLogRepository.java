package com.velo.courrier.predictive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EtaTrackingLogRepository extends JpaRepository<EtaTrackingLog, UUID> {
}
