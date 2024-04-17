package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.DeleteUserUseCase;
import org.example.individualbackend.persistance.UserRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepo userRepo;
    @Transactional
    @Override
    public void deleteUser(Integer id) {
        userRepo.deleteById(id);
    }
}
