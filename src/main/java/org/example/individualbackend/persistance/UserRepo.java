package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> getUserEntitiesBy();
    UserEntity save(UserEntity user);
    void deleteById(Integer id);
    boolean existsByEmail(String email);
    UserEntity getUserEntityById(Integer id);
    UserEntity findByEmail(String email);
}