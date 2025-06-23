package com.backendapp.cms.security.repository;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGrantedAuthorityRepository extends JpaRepository<UserGrantedAuthority, Long> {
    Optional<UserGrantedAuthority> findByAuthority(Authority authority);
}
