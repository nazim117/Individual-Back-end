package org.example.individualbackend.business.LoginService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.LoginService.Interface.LoginUseCase;
import org.example.individualbackend.business.GeneralExceptions.InvalidCredentialsException;
import org.example.individualbackend.config.db.conrs.security.token.AccessTokenEncoder;
import org.example.individualbackend.config.db.conrs.security.token.impl.AccessTokenImpl;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
import org.example.individualbackend.domain.login.RegisterResponse;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.UserRoleRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.RoleEnum;
import org.example.individualbackend.persistance.entity.UserEntity;

import org.example.individualbackend.persistance.entity.UserRoleEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final FanRepo fanRepository;
    private final UserRoleRepo userRoleRepository;

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

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }

        FanEntity fan = saveFan();
        UserEntity savedUser = saveNewUser(registerRequest, fan);

        RoleEnum role = RoleEnum.FOOTBALL_FAN;
        UserRoleEntity userRoleEntity =  saveUserRole(role, savedUser);

        Set<UserRoleEntity> userRoles = new HashSet<>();
        userRoles.add(userRoleEntity);
        savedUser.setUserRoles(userRoles);

        String accessToken = generateAccessToken(savedUser);
        return RegisterResponse.builder().accessToken(accessToken).build();
    }

    private UserRoleEntity saveUserRole(RoleEnum role, UserEntity savedUser) {
        return userRoleRepository.save(UserRoleEntity.builder().role(role).user(savedUser).build());
    }

    private UserEntity saveNewUser(RegisterRequest registerRequest, FanEntity fan) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        UserEntity userEntity = UserEntity
                .builder()
                .email(registerRequest.getEmail())
                .fName(registerRequest.getFName())
                .lName(registerRequest.getLName())
                .picture(registerRequest.getPicture())
                .password(encodedPassword)
                .fan(fan)
                .build();

        return userRepository.save(userEntity);
    }

    private FanEntity saveFan() {
        return fanRepository.save(FanEntity.builder().build());
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Integer userId = user.getId() != null ? user.getId() : null;
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, roles));
    }
}
