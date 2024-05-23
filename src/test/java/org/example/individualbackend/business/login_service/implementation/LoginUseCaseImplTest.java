package org.example.individualbackend.business.login_service.implementation;

import org.example.individualbackend.business.general_exceptions.InvalidCredentialsException;
import org.example.individualbackend.business.notifications_service.interfaces.NotificationsUseCase;
import org.example.individualbackend.config.security.token.AccessTokenEncoder;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.TokenResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.UserRoleRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.RoleEnum;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.example.individualbackend.persistance.entity.UserRoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


class LoginUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @Mock
    private FanRepo fanRepo;
    @Mock
    private UserRoleRepo userRoleRepo;
    @Mock
    private NotificationsUseCase notificationsUseCase;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_WithValidCredentials_ReturnsAccessToken(){
        String email = "test@test.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .id(1)
                .role(RoleEnum.FOOTBALL_FAN)
                .user(userEntity)
                .build();

        Set<UserRoleEntity> userRoleEntities = new HashSet<>();
        userRoleEntities.add(userRoleEntity);
        userEntity.setUserRoles(userRoleEntities);

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepo.findByEmail(email)).thenReturn(userEntity);
        when(passwordEncoder.matches(password,encodedPassword)).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn(accessToken);

        TokenResponse response = loginUseCase.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void login_WithInvalidCredentials_ThrowsInvalidCredentialsException(){
        //Arrange
        String email = "test@test.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .id(1)
                .role(RoleEnum.FOOTBALL_FAN)
                .user(userEntity)
                .build();

        Set<UserRoleEntity> userRoleEntities = new HashSet<>();
        userRoleEntities.add(userRoleEntity);
        userEntity.setUserRoles(userRoleEntities);

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepo.findByEmail(email)).thenReturn(userEntity);
        when(passwordEncoder.matches(password,encodedPassword)).thenReturn(false);

        //Act
        //Assert
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(request));
    }

    @Test
    void register_WithValidRequest_ReturnsRegisterResponse(){
        String email = "test@test.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";

        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(accessTokenEncoder.encode(any())).thenReturn(accessToken);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .id(1)
                .role(RoleEnum.FOOTBALL_FAN)
                .user(userEntity)
                .build();

        Set<UserRoleEntity> userRoleEntities = new HashSet<>();
        userRoleEntities.add(userRoleEntity);
        userEntity.setUserRoles(userRoleEntities);

        when(fanRepo.save(any())).thenReturn(FanEntity.builder().build());
        when(userRepo.save(any())).thenReturn(userEntity);
        when(userRoleRepo.save(any())).thenReturn(userRoleEntity);

        TokenResponse response = loginUseCase.register(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void register_WithExistingEmail_ThrowsResponseStatusException() {
        String email = "test@test.com";
        String password = "password";

        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepo.existsByEmail(email)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> loginUseCase.register(request));
    }
}