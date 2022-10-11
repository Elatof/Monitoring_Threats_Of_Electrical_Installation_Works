package com.korbiak.service.service.impl;


import com.korbiak.service.dto.AuthenticationRequest;
import com.korbiak.service.model.entities.User;
import com.korbiak.service.repos.UserRepo;
import com.korbiak.service.security.jwt.JwtTokenProvider;
import com.korbiak.service.service.AuthenticationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
@Data
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepo userRepo;
    private BCryptPasswordEncoder encoder;

    @PostConstruct
    private void initEncoder() {
        encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String login(AuthenticationRequest authenticationRequest) {
        log.info("Logging user with name = " + authenticationRequest.getFirstName());
        User user = userRepo.findByFirstNameAndSecondName(authenticationRequest.getFirstName(),
                authenticationRequest.getSecondName());
        if (authenticationRequest.getPassword().equals(user.getPassword())) {
            return jwtTokenProvider.createToken(user);
        }
        if (!encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            log.error("Password not correct");
            throw new IllegalArgumentException("Password not correct");
        }
        return jwtTokenProvider.createToken(user);
    }
}
