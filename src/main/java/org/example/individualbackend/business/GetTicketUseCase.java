package org.example.individualbackend.business;

import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;

public interface GetTicketUseCase {
    TicketEntity getTicket(Integer id);
}
