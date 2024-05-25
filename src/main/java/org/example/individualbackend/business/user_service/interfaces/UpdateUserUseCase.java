package org.example.individualbackend.business.user_service.interfaces;

import org.example.individualbackend.domain.update.UpdateUserRequest;

import java.nio.file.AccessDeniedException;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserRequest request) throws AccessDeniedException;
}
