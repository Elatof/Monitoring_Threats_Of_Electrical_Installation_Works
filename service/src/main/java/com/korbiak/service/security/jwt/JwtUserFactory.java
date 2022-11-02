package com.korbiak.service.security.jwt;

import com.korbiak.service.model.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getFirstName() + "_" + user.getSecondName(),
                user.getPassword(),
                user.getCompany().getId(),
                mapToGrantedAuthorities(user.getIsAdmin())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(int adminPermissions) {
        SimpleGrantedAuthority authority = null;
        if (adminPermissions == 1) {
            authority = new SimpleGrantedAuthority("USER");
        } else if (adminPermissions == 2) {
            authority = new SimpleGrantedAuthority("ADMIN");
        } else if (adminPermissions == 3) {
            authority = new SimpleGrantedAuthority("MAIN_ADMIN");
        }
        return Collections.singletonList(authority);
    }
}
