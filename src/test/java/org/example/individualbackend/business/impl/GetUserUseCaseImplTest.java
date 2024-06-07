package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.user_service.implementation.GetUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.config.security.token.AccessToken;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.AccessDeniedException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private void mockSecurityContext(String username){
        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void get_User_UserExists_ReturnsTrue() throws AccessDeniedException {
        int userId = 1;
        UserEntity mockUser = createMockUser(userId, "test@example.com");
        when(userRepo.getUserEntityById(userId)).thenReturn(mockUser);
        mockSecurityContext("test@example.com");

        UserEntity user = getUserUseCase.getUser(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    void getUser_UserDoesNotExist_ReturnsNull(){
        int nonExistentUserId = 999;
        when(userRepo.getUserEntityById(nonExistentUserId)).thenReturn(null);
        mockSecurityContext("test@example.com");

        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> getUserUseCase.getUser(nonExistentUserId));

        assertEquals("Invalid user id", exception.getMessage());
    }

    private UserEntity createMockUser(int userId, String email) {
        return UserEntity.builder()
                .id(userId)
                .email(email)
                .fName("John")
                .lName("Doe")
                .password("password123")
                .build();
    }
    @Test
    void getUser_AccessDenied_ThrowsAccessDeniedException(){
        int userId = 1;
        UserEntity mockUser = createMockUser(userId, "notAdminTest@example.com");
        when(userRepo.getUserEntityById(userId)).thenReturn(mockUser);
        mockSecurityContext("test@example.com");

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            getUserUseCase.getUser(userId);
        });

        assertEquals("You do not have permission to access this user", exception.getMessage());
    }
}