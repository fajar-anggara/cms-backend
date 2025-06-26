package com.backendapp.cms.security.entity;

import com.backendapp.cms.common.enums.Authority;
import com.backendapp.cms.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserGrantedAuthority implements GrantedAuthority {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAuthorityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.authority.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserGrantedAuthority that = (UserGrantedAuthority) o;
        return authority == that.authority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }

}