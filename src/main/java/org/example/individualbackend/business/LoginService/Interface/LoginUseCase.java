package org.example.individualbackend.business.LoginService.Interface;

import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
