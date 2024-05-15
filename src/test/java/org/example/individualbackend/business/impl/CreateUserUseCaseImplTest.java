package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.UserService.Implementation.CreateUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class CreateUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private FanRepo fanRepo;
    @Mock
    private UserRoleRepo userRoleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_createsUser_UserIsFan_Successful(){
        CreateUserRequest request = mock(CreateUserRequest.class);

        when(request.getRole()).thenReturn("FOOTBALL_FAN");

        Set<UserRoleEntity> userRoles = new HashSet<>();
        UserRoleEntity userRoleEntity1 = UserRoleEntity.builder().role(RoleEnum.FOOTBALL_FAN).build();
        UserRoleEntity userRoleEntity2 = UserRoleEntity.builder().role(RoleEnum.ADMIN).build();
        UserRoleEntity userRoleEntity3 = UserRoleEntity.builder().role(RoleEnum.CUSTOMER_SERVICE).build();
        userRoles.add(userRoleEntity1);
        userRoles.add(userRoleEntity2);
        userRoles.add(userRoleEntity3);

        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .userRoles(userRoles)
                .build();

        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(request.getRole()).thenReturn("FOOTBALL_FAN");


        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
        when(fanRepo.save(any(FanEntity.class))).thenReturn(FanEntity.builder().build());
        when(userRoleRepo.save(any(UserRoleEntity.class))).thenReturn(UserRoleEntity.builder()
                .role(RoleEnum.FOOTBALL_FAN)
                .user(userEntity)
                .build());
        userEntity.setId(1);

        CreateUserResponse response = createUserUseCase.createUser(request);

        assertNotNull(response);
        assertNotNull(response.getId());
    }

    @Test
    void create_createUser_Failure_UserExists(){
        CreateUserRequest request = CreateUserRequest.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();

        when(userRepo.existsByEmail(any())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> createUserUseCase.createUser(request));
    }

    @Test
    void create_createsUser_UserIsAdmin_Successful(){
        CreateUserRequest request = mock(CreateUserRequest.class);

        when(request.getRole()).thenReturn("ADMIN");

        Set<UserRoleEntity> userRoles = new HashSet<>();
        UserRoleEntity userRoleEntity1 = UserRoleEntity.builder().role(RoleEnum.FOOTBALL_FAN).build();
        UserRoleEntity userRoleEntity2 = UserRoleEntity.builder().role(RoleEnum.ADMIN).build();
        UserRoleEntity userRoleEntity3 = UserRoleEntity.builder().role(RoleEnum.CUSTOMER_SERVICE).build();
        userRoles.add(userRoleEntity1);
        userRoles.add(userRoleEntity2);
        userRoles.add(userRoleEntity3);

        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .userRoles(userRoles)
                .build();

        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(request.getRole()).thenReturn("ADMIN");


        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userRoleRepo.save(any(UserRoleEntity.class))).thenReturn(UserRoleEntity.builder()
                .role(RoleEnum.ADMIN)
                .user(userEntity)
                .build());
        userEntity.setId(1);

        CreateUserResponse response = createUserUseCase.createUser(request);

        assertNotNull(response);
        assertNotNull(response.getId());

    }

    @Test
    void create_createsUser_UserIsCustomerService_Successful(){
        CreateUserRequest request = mock(CreateUserRequest.class);

        when(request.getRole()).thenReturn("CUSTOMER_SERVICE");

        Set<UserRoleEntity> userRoles = new HashSet<>();
        UserRoleEntity userRoleEntity1 = UserRoleEntity.builder().role(RoleEnum.FOOTBALL_FAN).build();
        UserRoleEntity userRoleEntity2 = UserRoleEntity.builder().role(RoleEnum.ADMIN).build();
        UserRoleEntity userRoleEntity3 = UserRoleEntity.builder().role(RoleEnum.CUSTOMER_SERVICE).build();
        userRoles.add(userRoleEntity1);
        userRoles.add(userRoleEntity2);
        userRoles.add(userRoleEntity3);

        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .userRoles(userRoles)
                .build();

        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(request.getRole()).thenReturn("CUSTOMER_SERVICE");


        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userRoleRepo.save(any(UserRoleEntity.class))).thenReturn(UserRoleEntity.builder()
                .role(RoleEnum.ADMIN)
                .user(userEntity)
                .build());
        userEntity.setId(1);

        CreateUserResponse response = createUserUseCase.createUser(request);

        assertNotNull(response);
        assertNotNull(response.getId());

    }
}