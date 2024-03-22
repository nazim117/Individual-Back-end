package org.example.individualbackend.persistance.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class FakeTicketRepoImpl implements TicketRepo {
    private static Integer NEXT_ID=1;
    private final List<TicketEntity> savedTickets;
    @Override
    public List<TicketEntity> getAllTickets() {return Collections.unmodifiableList(savedTickets);}

    @Override
    public TicketEntity findById(Integer id) {
        return savedTickets
                .stream()
                .filter(ticketEntity -> Objects.equals(ticketEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public TicketEntity update(TicketEntity ticket) {

        TicketEntity ticketEntity = findById(ticket.getId());
        ticketEntity.setPrice(ticket.getPrice());
        ticketEntity.setRowNum(ticket.getRowNum());
        ticketEntity.setSeatNumber(ticket.getSeatNumber());

        return ticketEntity;
    }

    @Override
    public void delete(Integer id) {
        savedTickets.removeIf(userEntity -> Objects.equals(userEntity.getId(), id));
    }
}
