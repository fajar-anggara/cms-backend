package com.backendapp.cms.users.repository;

import com.backendapp.cms.security.entity.RefreshPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshPasswordTokenCrudRepository extends JpaRepository<RefreshPasswordTokenEntity, Long> {

    boolean existsByToken(String token);

    Optional<RefreshPasswordTokenEntity> findByToken(String token);
}
