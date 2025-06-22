package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCrudRepository extends JpaRepository<UserEntity, Long> {
}
