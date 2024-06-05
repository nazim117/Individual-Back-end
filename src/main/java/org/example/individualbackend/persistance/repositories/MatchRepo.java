package org.example.individualbackend.persistance.repositories;

import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<MatchEntity, Integer> {
    List<MatchEntity> findAllByOrderByDateAsc();
    List<MatchEntity> findAllByOrderByDateDesc();
    @Query("SELECT m " +
            "FROM MatchEntity m " +
            "LEFT JOIN m.availableTickets t " +
            "WHERE t.fan.id IS NOT NULL " +
            "GROUP BY m " +
            "ORDER BY COUNT(t.id) DESC, m.date")
    List<MatchEntity> findAllByMostSoldTickets();
    @Query("SELECT m FROM MatchEntity m WHERE m.date >= CURRENT_TIMESTAMP ORDER BY m.date ASC")
    List<MatchEntity> find6UpcomingMatches();
    MatchEntity getMatchEntityById(Integer id);
}
