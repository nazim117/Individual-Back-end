package org.example.individualbackend.business.user_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.user_service.interfaces.GetUsersUseCase;
import org.example.individualbackend.business.user_service.utilities.UserConverter;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public GetAllUsersResponse getUsers() {
        List<User> users = userRepo.getUserEntitiesBy()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetAllUsersResponse
                .builder()
                .users(users)
                .build();
    }
}
