package org.example.individualbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.LoginService.Interface.LoginUseCase;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class LoginController {
    private final LoginUseCase loginUseCase;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        if(loginResponse != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
        }
        return  ResponseEntity.badRequest().build();

    }
}
