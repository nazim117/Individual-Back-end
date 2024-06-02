package org.example.individualbackend.persistance.repositories;

import org.example.individualbackend.persistance.entity.UserEntity;
import org.example.individualbackend.persistance.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRoleEntity, Integer> {
    UserRoleEntity save(UserRoleEntity userRole);
    void deleteUserRoleEntityByUser(UserEntity user);
}
