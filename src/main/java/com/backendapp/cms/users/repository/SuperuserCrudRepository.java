package com.backendapp.cms.users.repository;

import com.backendapp.cms.users.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperuserCrudRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    // deleted
    @Query(value = "SELECT * FROM UserEntity WHERE deleted_at IS NOT NULL",
            countQuery = "SELECT count(*) FROM UserEntity WHERE deleted_at IS NOT NULL",
            nativeQuery = true)
    Page<UserEntity> findAllDeleted(Pageable pageable);

    // deleted and non deleted
    @Query(value = "SELECT * FROM UserEntity",
            countQuery = "SELECT count(*) FROM UserEntity",
            nativeQuery = true)
    Page<UserEntity> findAllWithDeleted(Pageable pageable);



}
