package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<MatchEntity, Integer> {
    List<MatchEntity> findAllByOrderByDateAsc();
    @Query("SELECT m FROM MatchEntity m WHERE m.date >= CURRENT_TIMESTAMP ORDER BY m.date ASC")
    List<MatchEntity> find3UpcomingMatches();
    MatchEntity getMatchEntityById(Integer id);
}
