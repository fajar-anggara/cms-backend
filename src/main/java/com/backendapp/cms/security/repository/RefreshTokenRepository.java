package com.backendapp.cms.security.repository;

import com.backendapp.cms.security.entity.RefreshTokenEntity;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findById(String id);

    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    @Override
    List<RefreshTokenEntity> findAll();
}
