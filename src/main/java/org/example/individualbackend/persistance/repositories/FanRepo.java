package org.example.individualbackend.persistance.repositories;

import org.example.individualbackend.persistance.entity.FanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FanRepo extends JpaRepository<FanEntity, Integer> {
    FanEntity save(FanEntity fan);
    FanEntity findById(int id);
}
