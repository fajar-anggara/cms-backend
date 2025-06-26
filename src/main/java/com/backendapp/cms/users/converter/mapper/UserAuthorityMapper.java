package com.backendapp.cms.users.converter.mapper;

import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.security.entity.UserGrantedAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public class UserAuthorityMapper {

    @Named("mapUserGrantedAuthorityToUserSimpleResponseAuthorityEnum")
    public UserSimpleResponse.AuthorityEnum mapUserGrantedAuthorityToUserSimpleResponseAuthorityEnum(UserGrantedAuthority userGrantedAuthority) {
        if(userGrantedAuthority == null || userGrantedAuthority.getAuthority() == null) {
            return null;
        }

        try {
            String authorityString = userGrantedAuthority.getAuthority();
            if(authorityString.startsWith("ROLE_")) {
                authorityString = authorityString.substring(5);
            }

            return UserSimpleResponse.AuthorityEnum.valueOf(authorityString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("tidak bisa mapping authority UserGrantedAuthority ke authority UserSimpleResponse.AuthorityEnum");
            return null;
        }

    }
}
