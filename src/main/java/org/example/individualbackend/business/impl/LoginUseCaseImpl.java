package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.LoginUseCase;
import org.example.individualbackend.business.exception.InvalidCredentialsException;
import org.example.individualbackend.config.db.conrs.security.token.AccessTokenEncoder;
import org.example.individualbackend.config.db.conrs.security.token.impl.AccessTokenImpl;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;

import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());
//        if (user.getUserRoles() != null) {
//            Hibernate.initialize(user.getUserRoles());
//            Hibernate.initialize(user.getFan());
//        }
        if (user == null) {
            throw new InvalidCredentialsException();
        }
        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        //return passwordEncoder.matches(rawPassword, encodedPassword);
        return rawPassword.equals(encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Long userId = user.getFan() != null ? user.getFan().getId() : null;
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, roles));
    }
}
