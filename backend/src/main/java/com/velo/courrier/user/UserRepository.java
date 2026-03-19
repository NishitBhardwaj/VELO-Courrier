package com.velo.courrier.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPhone(String phone);
}
