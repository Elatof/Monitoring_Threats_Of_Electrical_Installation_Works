package com.korbiak.service.security;

import com.korbiak.service.model.entities.User;
import com.korbiak.service.repos.UserRepo;
import com.korbiak.service.security.jwt.JwtUserFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Data
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by name = " + username);
        String[] names = username.split("_");
        User user = userRepo.findByFirstNameAndSecondName(names[0], names[1]);
        return JwtUserFactory.create(user);
    }
}
