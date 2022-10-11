package com.korbiak.service.service;

import com.korbiak.service.dto.AuthenticationRequest;

public interface AuthenticationService {

    String login(AuthenticationRequest authenticationRequest);
}
