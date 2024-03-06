package org.example.individualbackend.business;

import org.example.individualbackend.domain.update.UpdateUserRequest;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserRequest request);
}
