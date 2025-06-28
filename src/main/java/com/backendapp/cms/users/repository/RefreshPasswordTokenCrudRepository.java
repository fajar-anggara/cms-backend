package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.RefreshPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshPasswordTokenCrudRepository extends JpaRepository<RefreshPasswordTokenEntity, Long> {

}
