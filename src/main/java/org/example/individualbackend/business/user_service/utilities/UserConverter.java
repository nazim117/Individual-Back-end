package org.example.individualbackend.business.user_service.utilities;

import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.UserEntity;

public class UserConverter {
    private UserConverter(){

    }
    public static User convert(UserEntity user){
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fName(user.getFName())
                .lName(user.getLName())
                .password(user.getPassword())
                .build();
    }
}
