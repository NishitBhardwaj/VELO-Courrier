package com.velo.courrier.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, UUID> {
}
