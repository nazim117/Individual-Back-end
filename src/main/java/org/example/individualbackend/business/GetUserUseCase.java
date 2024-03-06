package org.example.individualbackend.business;

import org.example.individualbackend.persistance.entity.UserEntity;

public interface GetUserUseCase {
    UserEntity getUser(Integer id);
}
