package com.velo.courrier.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ZoneEventRepository extends JpaRepository<ZoneEvent, UUID> {
}
