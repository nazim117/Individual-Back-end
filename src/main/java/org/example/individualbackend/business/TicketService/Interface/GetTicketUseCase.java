package org.example.individualbackend.business.TicketService.Interface;

import org.example.individualbackend.persistance.entity.TicketEntity;


public interface GetTicketUseCase {
    TicketEntity getTicket(Integer id);

}
