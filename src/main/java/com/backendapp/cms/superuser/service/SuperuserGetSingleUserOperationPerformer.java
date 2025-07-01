package com.backendapp.cms.superuser.service;

import com.backendapp.cms.users.entity.UserEntity;
import com.backendapp.cms.users.exception.UsernameOrEmailNotFoundException;
import com.backendapp.cms.users.repository.UserCrudRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SuperuserGetSingleUserOperationPerformer {

    private final UserCrudRepository userCrudRepository;

    public UserEntity getSingleUser(Long id) {
        return userCrudRepository.findById(id)
                .orElseThrow(UsernameOrEmailNotFoundException::new);
    }
}
