package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.UserService.Implementation.CreateUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class CreateUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_createsUser_Successful(){
        CreateUserRequest request = CreateUserRequest.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();

        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
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
}