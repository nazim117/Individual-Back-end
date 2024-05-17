package org.example.individualbackend.business.ticket_service.interfaces;

import org.example.individualbackend.persistance.entity.TicketEntity;


public interface GetTicketUseCase {
    TicketEntity getTicket(Integer id);

}
