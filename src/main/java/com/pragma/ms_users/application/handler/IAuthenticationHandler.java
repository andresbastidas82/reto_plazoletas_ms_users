package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.AuthenticationRequest;
import com.pragma.ms_users.application.dto.AuthenticationResponse;

public interface IAuthenticationHandler {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
