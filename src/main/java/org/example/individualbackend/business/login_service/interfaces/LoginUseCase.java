package org.example.individualbackend.business.login_service.interfaces;

import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.TokenResponse;
import org.example.individualbackend.domain.login.RegisterRequest;

public interface LoginUseCase {
    TokenResponse login(LoginRequest loginRequest);

    TokenResponse register(RegisterRequest registerRequest);
}
