package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.user_service.implementation.GetUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_User_UserExists_ReturnsTrue() throws AccessDeniedException {
        int userId = 1;
        UserEntity mockUser = createMockUser(userId);
        when(userRepo.getUserEntityById(userId)).thenReturn(mockUser);

        UserEntity user = getUserUseCase.getUser(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    void get_User_UserDoesNotExist_ReturnsNull()throws AccessDeniedException{
        int nonExistentUserId = 999;
        when(userRepo.getUserEntityById(nonExistentUserId)).thenReturn(null);

        UserEntity user = getUserUseCase.getUser(nonExistentUserId);

        assertEquals(null, user);
    }

    private UserEntity createMockUser(int userId) {
        return UserEntity.builder()
                .id(userId)
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();
    }
}