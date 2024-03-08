package org.example.individualbackend.business.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UpdateUserUseCase;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepo userRepo;
    @Override
    public void updateUser(UpdateUserRequest request) {
        UserEntity userEntity = userRepo.findById(request.getId());
        if(userEntity == null){
            throw new NullPointerException("User_ID_INVALID");
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
        userRepo.update(user);
    }
}
