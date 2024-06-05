package org.example.individualbackend.business.user_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.user_service.interfaces.DeleteUserUseCase;
import org.example.individualbackend.persistance.repositories.FanRepo;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.repositories.UserRoleRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepo userRepo;
    private final FanRepo fanRepo;
    private final UserRoleRepo userRoleRepo;
    @Transactional
    @Override
    public void deleteUser(Integer id) {
        UserEntity user = userRepo.findById(id).orElse(null);
        if(user != null) {
            fanRepo.deleteById(user.getFan().getId());
            userRepo.deleteById(id);
            userRoleRepo.deleteUserRoleEntityByUser(user);

        }
    }
}
