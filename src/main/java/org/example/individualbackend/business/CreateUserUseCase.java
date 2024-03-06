package org.example.individualbackend.business;

import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest request);
}
