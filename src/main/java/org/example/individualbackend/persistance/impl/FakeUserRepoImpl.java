package org.example.individualbackend.persistance.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class FakeUserRepoImpl implements UserRepo {
    private static Integer NEXT_ID=1;
    private final List<UserEntity> savedUsers;
    @Override
    public List<UserEntity> getAll() {
        return Collections.unmodifiableList(savedUsers);
    }

    @Override
    public UserEntity findById(Integer id) {
        return savedUsers
                .stream()
                .filter(userEntity -> Objects.equals(userEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserEntity save(UserEntity user) {
        user.setId(NEXT_ID);
        NEXT_ID++;
        savedUsers.add(user);
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
        UserEntity userEntity = findById(user.getId());
        userEntity.setFName(user.getFName());
        userEntity.setLName(user.getLName());
        userEntity.setPicture(user.getPicture());
        userEntity.setPassword(user.getPassword());
        userEntity.setTickets(user.getTickets());

        return userEntity;
    }

    @Override
    public void delete(Integer id) {
        savedUsers.removeIf(userEntity -> Objects.equals(userEntity.getId(), id));
    }

    @Override
    public int count() {
        return savedUsers.size();
    }

    @Override
    public boolean isSavedByEmail(String email) {
        return savedUsers
                .stream()
                .anyMatch(userEntity -> userEntity.getEmail().equals(email));
    }
}
