package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.UserService.Implementation.DeleteUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class DeleteUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void delete_User_ValidId_DeletesUser(){
        int userId = 1;

        assertDoesNotThrow(() -> deleteUserUseCase.deleteUser(userId));

        verify(userRepo).deleteById(userId);
    }

    @Test
    void delete_User_InvalidId_ThrowsException(){
        int invalidUserId = 1;

        doThrow(EmptyResultDataAccessException.class).when(userRepo).deleteById(invalidUserId);

        assertThrows(EmptyResultDataAccessException.class, () -> deleteUserUseCase.deleteUser(invalidUserId));
    }
}