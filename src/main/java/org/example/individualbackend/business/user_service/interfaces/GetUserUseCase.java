package org.example.individualbackend.business.user_service.interfaces;

import org.example.individualbackend.persistance.entity.UserEntity;

public interface GetUserUseCase {
    UserEntity getUser(Integer id);
}
