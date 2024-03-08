package org.example.individualbackend.business.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetUsersUseCase;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private final UserRepo userRepo;
    @Override
    public GetAllUsersResponse getUsers() {
        List<User> users = userRepo.getAll()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetAllUsersResponse
                .builder()
                .users(users)
                .build();
    }
}
