package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface UserCrudRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username); // <-- Perbaiki di sini
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
