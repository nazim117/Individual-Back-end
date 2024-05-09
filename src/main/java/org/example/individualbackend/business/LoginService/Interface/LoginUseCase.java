package org.example.individualbackend.business.LoginService.Interface;

import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
import org.example.individualbackend.domain.login.RegisterResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest registerRequest);
}
