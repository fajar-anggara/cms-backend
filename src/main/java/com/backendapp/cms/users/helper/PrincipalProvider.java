package com.backendapp.cms.users.helper;


import com.backendapp.cms.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PrincipalProvider {

    public UserEntity getPrincipal() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
