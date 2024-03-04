package org.example.individualbackend.business.iml;

import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.UserEntity;

public class UserConverter {
    public static User convert(UserEntity user){
        return User.builder()
                .id(user.getId())
                .fName(user.getFName())
                .lName(user.getLName())
                .password(user.getPassword())
                .picture(user.getPicture())
                .tickets(user.getTickets())
                .build();
    }
}
