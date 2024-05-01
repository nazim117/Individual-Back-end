package org.example.individualbackend.business.LoginService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.LoginService.Interface.LoginUseCase;
import org.example.individualbackend.business.GeneralExceptions.InvalidCredentialsException;
import org.example.individualbackend.config.db.conrs.security.token.AccessTokenEncoder;
import org.example.individualbackend.config.db.conrs.security.token.impl.AccessTokenImpl;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());


        if (user == null || !matchesPassword(loginRequest.getPassword(), user.getPassword())) {
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
        Integer userId = user.getFan() != null ? user.getFan().getId() : null;
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, roles));
    }
}
