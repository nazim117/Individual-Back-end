package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<MatchEntity, Integer> {
    List<MatchEntity> getMatchEntitiesBy();
    MatchEntity getMatchEntityById(Integer id);
}
