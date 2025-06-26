package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCrudRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username); // <-- Perbaiki di sini
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
}
