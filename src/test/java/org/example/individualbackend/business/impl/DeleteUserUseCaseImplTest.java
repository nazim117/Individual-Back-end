package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.user_service.implementation.DeleteUserUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.repositories.FanRepo;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.repositories.UserRoleRepo;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class DeleteUserUseCaseImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private FanRepo fanRepo;
    @Mock
    private UserRoleRepo userRoleRepo;
    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;
    @Autowired
    private DeleteUserUseCaseImpl deleteUserUseCaseImpl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void delete_User_ValidId_DeletesUser(){
        int userId = 1;


        FanEntity fanEntity = FanEntity.builder().id(1).build();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .fan(fanEntity)
                .build();

        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .id(1)
                .role(RoleEnum.FOOTBALL_FAN)
                .user(userEntity)
                .build();

        Set<UserRoleEntity> userRoleEntities = new HashSet<>();
        userRoleEntities.add(userRoleEntity);
        userEntity.setUserRoles(userRoleEntities);

        when(userRepo.findById(userId)).thenReturn(Optional.of(userEntity));

        assertDoesNotThrow(() -> deleteUserUseCase.deleteUser(userId));

        verify(fanRepo).deleteById(fanEntity.getId());
        verify(userRepo).deleteById(userId);
        verify(userRoleRepo).deleteUserRoleEntityByUser(userEntity);
    }

    @Test
    void delete_User_InvalidId_ThrowsException(){
        int invalidUserId = -1;

        when(userRepo.findById(invalidUserId)).thenReturn(Optional.empty());

        deleteUserUseCase.deleteUser(invalidUserId);

        verify(fanRepo, never()).deleteById(anyInt());
        verify(userRepo, never()).deleteById(anyInt());
        verify(userRoleRepo, never()).deleteUserRoleEntityByUser(any(UserEntity.class));
    }
}