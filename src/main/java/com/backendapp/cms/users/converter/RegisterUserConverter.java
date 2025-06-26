package com.backendapp.cms.users.converter;

import com.backendapp.cms.openapi.dto.UserDTO;
import com.backendapp.cms.openapi.dto.UserRegisterRequest;
import com.backendapp.cms.openapi.dto.UserSimpleResponse;
import com.backendapp.cms.users.converter.mapper.UserAuthorityMapper;
import com.backendapp.cms.users.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserAuthorityMapper.class})
public interface RegisterUserConverter {

    // UserDTO toDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "authority", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "isEmailVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    UserEntity toEntity(UserRegisterRequest userRegisterRequest);

    @Mapping(source = "authority", target = "authority", qualifiedByName = "mapUserGrantedAuthorityToUserSimpleResponseAuthorityEnum")
    UserSimpleResponse toSimpleResponse(UserEntity userEntity);

    /** @Named("mapUserGrantedAuthorityToUserDTOEnum") // Beri nama yang lebih deskriptif
    default UserDTO.AuthorityEnum mapUserGrantedAuthorityToUserDTOEnum(UserGrantedAuthority userGrantedAuthority) {
        if (userGrantedAuthority == null || userGrantedAuthority.getAuthority() == null) {
            return null; // Atau UserDTO.AuthorityEnum.UNKNOWN jika ada
        }
        try {
            String authorityString = userGrantedAuthority.getAuthority();

            // Jika authority Anda datang dengan prefix "ROLE_", Anda mungkin ingin menghapusnya
            // Contoh: "ROLE_ADMIN" menjadi "ADMIN"
            if (authorityString.startsWith("ROLE_")) {
                authorityString = authorityString.substring(5); // Hapus "ROLE_"
            }
            return UserDTO.AuthorityEnum.valueOf(authorityString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Cannot map authority string: '" + userGrantedAuthority.getAuthority() + "' to UserDTO.AuthorityEnum. Error: " + e.getMessage());
            // Anda bisa log error ini lebih baik atau melempar pengecualian kustom
            return null; // Atau kembalikan default, misal UserDTO.AuthorityEnum.USER
        }
    }
     **/

}