package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.user_service.implementation.GetUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private void mockSecurityContext(String username){
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void get_User_UserExists_ReturnsTrue() throws AccessDeniedException {
        int userId = 1;
        UserEntity mockUser = createMockUser(userId);
        when(userRepo.getUserEntityById(userId)).thenReturn(mockUser);
        mockSecurityContext("test@example.com");

        UserEntity user = getUserUseCase.getUser(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    void get_User_UserDoesNotExist_ReturnsNull()throws AccessDeniedException{
        int nonExistentUserId = 999;
        when(userRepo.getUserEntityById(nonExistentUserId)).thenReturn(null);
        mockSecurityContext("test@example.com");

        Exception exception =
                assertThrows(NullPointerException.class, () -> getUserUseCase.getUser(nonExistentUserId));

        assertEquals("Invalid user id", exception.getMessage());
    }

    private UserEntity createMockUser(int userId) {
        return UserEntity.builder()
                .id(userId)
                .email("test@example.com")
                .fName("John")
                .lName("Doe")
                .password("password123")
                .build();
    }
}