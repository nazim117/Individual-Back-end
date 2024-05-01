package org.example.individualbackend.business.UserService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UserService.Interface.GetUserUseCase;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private final UserRepo userRepo;
    @Transactional
    @Override
    public UserEntity getUser(Integer id) {
        return userRepo.getUserEntityById(id);
    }
}
