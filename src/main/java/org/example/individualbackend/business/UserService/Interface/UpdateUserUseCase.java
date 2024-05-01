package org.example.individualbackend.business.UserService.Interface;

import org.example.individualbackend.domain.update.UpdateUserRequest;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserRequest request);
}
