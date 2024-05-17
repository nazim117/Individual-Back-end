package org.example.individualbackend.business.user_service.interfaces;

import org.example.individualbackend.domain.update.UpdateUserRequest;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserRequest request);
}
