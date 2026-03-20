package com.velo.courrier.governance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DriverDocumentRepository extends JpaRepository<DriverDocument, UUID> {
}
