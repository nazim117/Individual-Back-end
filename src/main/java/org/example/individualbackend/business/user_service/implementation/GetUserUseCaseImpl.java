package org.example.individualbackend.business.user_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.user_service.interfaces.GetUserUseCase;
import org.example.individualbackend.config.security.SecurityUtils;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private final UserRepo userRepo;
    @Transactional
    @Override
    public UserEntity getUser(Integer id) throws AccessDeniedException {
        UserEntity userEntity =  userRepo.getUserEntityById(id);
        if(userEntity == null){
            throw new NullPointerException("Invalid user id");
        }

        if(!currentUserHasPermission(userEntity)){
            throw new AccessDeniedException("You do not have permission to access this user");
        }

        return userEntity;
    }

    private boolean currentUserHasPermission(UserEntity userEntity) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        return currentUsername.equals(userEntity.getEmail());
    }
}
