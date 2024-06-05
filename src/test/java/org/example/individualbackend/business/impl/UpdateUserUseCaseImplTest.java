package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.user_service.implementation.UpdateUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class UpdateUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void update_User_ValidRequest_UpdatesUserFields_ReturnsTrue(){
        int userId = 1;
        UpdateUserRequest request = createUpdateUserRequest(userId);
        UserEntity existingUser = createMockUserEntity(userId);

        when(userRepo.getUserEntityById(userId)).thenReturn(existingUser);
        when(userRepo.save(existingUser)).thenReturn(existingUser);

        assertDoesNotThrow(() -> updateUserUseCase.updateUser(request));

        verify(userRepo).save(existingUser);
        assertEquals(request.getId(), existingUser.getId());
        assertEquals(request.getEmail(), existingUser.getEmail());
        assertEquals(request.getFName(), existingUser.getFName());
        assertEquals(request.getLName(), existingUser.getLName());
        assertEquals(request.getPicture(), existingUser.getPicture());
        assertEquals(request.getPassword(), existingUser.getPassword());

    }


    @Test
    void update_User_InvalidUserId_ThrowsException(){
        int invalidUserId = 998;
        UpdateUserRequest request = createUpdateUserRequest(invalidUserId);

        when(userRepo.getUserEntityById(invalidUserId)).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> updateUserUseCase.updateUser(request));
        assertEquals("User_ID_INVALID", exception.getMessage());
        verify(userRepo, never()).save(any());
    }
    private UserEntity createMockUserEntity(int userId) {
        return UserEntity.builder()
                .id(userId)
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();
    }

    private UpdateUserRequest createUpdateUserRequest(int userId) {
        return UpdateUserRequest.builder()
                .id(userId)
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .picture("pic.jpg")
                .password("password123")
                .build();
    }
}