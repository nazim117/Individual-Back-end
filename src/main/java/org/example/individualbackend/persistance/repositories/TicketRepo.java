package org.example.individualbackend.persistance.repositories;

import org.example.individualbackend.domain.match.MatchRevenueData;
import org.example.individualbackend.domain.match.MatchTicketData;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<TicketEntity, Integer> {
    TicketEntity save(TicketEntity ticketEntity);

    @Query("SELECT t FROM TicketEntity t WHERE t.footballMatch.id = :matchId AND t.fan IS NULL")
    List<TicketEntity> findByFootballMatchId(int matchId);

    void deleteById(Integer id);

    TicketEntity getTicketEntityById(Integer id);

    boolean existsByRowNumAndSeatNumber(Integer rowNum, Integer seatNum);

    List<TicketEntity> findByFan_Id(Integer userId);

    @Query("SELECT COUNT(t) " +
            "FROM TicketEntity t " +
            "WHERE t.fan.id IS NOT NULL")
    long countTotalTicketsSold();

    @Query("SELECT SUM(t.price) " +
            "FROM TicketEntity t " +
            "WHERE t.fan.id IS NOT NULL")
    Double sumTotalRevenue();

    @Query("SELECT new org.example.individualbackend.domain.match.MatchTicketData(m.id, COUNT(t)) " +
            "FROM TicketEntity t " +
            "JOIN t.footballMatch m " +
            "GROUP BY m.id")
    List<MatchTicketData> countTicketsPerMatch();

    @Query("SELECT new org.example.individualbackend.domain.match.MatchRevenueData(m.id, SUM(t.price)) " +
            "FROM TicketEntity t " +
            "JOIN t.footballMatch m " +
            "WHERE t.fan IS NOT NULL " +
            "GROUP BY t.footballMatch.id")
    List<MatchRevenueData> sumRevenuesPerMatch();
}
