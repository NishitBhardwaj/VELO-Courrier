package com.velo.courrier.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserTrustScoreRepository extends JpaRepository<UserTrustScore, UUID> {
}
