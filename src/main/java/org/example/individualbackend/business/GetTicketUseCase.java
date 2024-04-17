package org.example.individualbackend.business;

import org.example.individualbackend.persistance.entity.TicketEntity;

public interface GetTicketUseCase {
    TicketEntity getTicket(Integer id);
}
