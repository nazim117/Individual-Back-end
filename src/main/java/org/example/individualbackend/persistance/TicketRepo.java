package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.TicketEntity;

import java.util.List;

public interface TicketRepo {
    List<TicketEntity> getAllTickets();

    TicketEntity findById(Integer id);

    TicketEntity update(TicketEntity ticketEntity);

    void delete(Integer id);
}
