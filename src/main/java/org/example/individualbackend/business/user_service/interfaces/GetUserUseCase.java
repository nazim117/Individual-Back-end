package org.example.individualbackend.business.user_service.interfaces;

import org.example.individualbackend.persistance.entity.UserEntity;

import java.nio.file.AccessDeniedException;

public interface GetUserUseCase {
    UserEntity getUser(Integer id) throws AccessDeniedException;
}
