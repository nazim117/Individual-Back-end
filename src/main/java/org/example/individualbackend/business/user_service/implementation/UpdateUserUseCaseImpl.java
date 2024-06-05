package org.example.individualbackend.business.user_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.user_service.interfaces.UpdateUserUseCase;
import org.example.individualbackend.config.security.SecurityUtils;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepo userRepo;
    @Transactional
    @Override
    public void updateUser(UpdateUserRequest request) throws AccessDeniedException {
        UserEntity userEntity = userRepo.getUserEntityById(request.getId());
        if(userEntity == null){
            throw new NullPointerException("User_ID_INVALID");
        }

        if(!currentUserHasPermission(userEntity)){
            throw new AccessDeniedException("You do not have permission to access this user");
        }

        updateFields(request, userEntity);
    }
    private void updateFields(UpdateUserRequest request, UserEntity user){
        user.setId(request.getId());
        user.setEmail(request.getEmail());
        user.setFName(request.getFName());
        user.setLName(request.getLName());
        user.setPassword(request.getPassword());
        user.setPicture(request.getPicture());
        userRepo.save(user);
    }

    private boolean currentUserHasPermission(UserEntity userEntity) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        return currentUsername.equals(userEntity.getEmail());
    }
}
