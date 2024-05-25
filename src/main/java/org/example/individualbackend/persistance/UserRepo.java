package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u " +
            "FROM UserEntity u " +
            "WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(u.fName) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(u.lName) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<UserEntity> searchUserEntitiesBy(@Param("searchString")String searchString);
    List<UserEntity> getUserEntitiesBy();
    UserEntity save(UserEntity user);
    void deleteById(Integer id);
    boolean existsByEmail(String email);
    UserEntity getUserEntityById(Integer id);
    UserEntity findByEmail(String email);
}