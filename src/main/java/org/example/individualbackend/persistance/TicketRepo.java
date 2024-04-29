package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TicketRepo extends JpaRepository<TicketEntity, Integer> {
    //@Query("SELECT t FROM TicketEntity t JOIN FETCH t.footballMatch.id JOIN FETCH t.fan.id")
    List<TicketEntity> findAllBy();
    TicketEntity save(TicketEntity ticketEntity);
    void deleteById(Integer id);
    TicketEntity getTicketEntityById(Integer id);
    boolean existsByRowNumAndSeatNumber(Integer rowNum, Integer seatNum);
}
