package org.example.individualbackend.business.UserService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UserService.Interface.GetUsersUseCase;
import org.example.individualbackend.business.UserService.Utilities.UserConverter;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private final UserRepo userRepo;
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
