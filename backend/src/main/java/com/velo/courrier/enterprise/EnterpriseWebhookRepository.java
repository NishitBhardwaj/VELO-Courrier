package com.velo.courrier.enterprise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EnterpriseWebhookRepository extends JpaRepository<EnterpriseWebhook, UUID> {
}
