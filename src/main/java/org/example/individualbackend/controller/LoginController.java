package org.example.individualbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.login_service.interfaces.LoginUseCase;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.TokenResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
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
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try{
            TokenResponse tokenResponse = loginUseCase.login(loginRequest);
            if(tokenResponse != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
            }
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody @Valid RegisterRequest registerRequest){
        try{
            TokenResponse registerResponse = loginUseCase.register(registerRequest);
            if(registerResponse != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
