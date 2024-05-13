package org.example.individualbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.LoginService.Interface.LoginUseCase;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
import org.example.individualbackend.domain.login.RegisterResponse;
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
        try{
            LoginResponse loginResponse = loginUseCase.login(loginRequest);
            if(loginResponse != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
            }
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest){
        try{
            RegisterResponse registerResponse = loginUseCase.register(registerRequest);
            if(registerResponse != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
