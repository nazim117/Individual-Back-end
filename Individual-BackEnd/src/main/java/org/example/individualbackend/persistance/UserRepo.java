package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.UserEntity;

import java.util.List;

public interface UserRepo {
    List<UserEntity> getAll();
    UserEntity findById(Integer id);
    public UserEntity save(UserEntity match);
    UserEntity update(UserEntity user);
    void delete(Integer id);
    int count();
    boolean isSavedByPassword(String password);
}
