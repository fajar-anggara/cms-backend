package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCrudRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndDeletedAtIsNull(String username);

    boolean existsByUsernameAndDeletedAtIsNull(@NotNull @Pattern(regexp = "^[a-zA-Z0-9_-]+$") @Size(min = 1, max = 255) String username);

    boolean existsByEmailAndDeletedAtIsNull(@NotNull @Size(min = 1, max = 255) @Email String email);

    boolean existsByUsername(@NotNull @Pattern(regexp = "^[a-zA-Z0-9_-]+$") @Size(min = 1, max = 255) String username);

    boolean existsByEmail(@NotNull @Size(min = 1, max = 255) @Email String email);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String identifier);
}
