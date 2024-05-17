package org.example.individualbackend.business.ticket_service.interfaces;

import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;

public interface CreateTicketUseCase {
    CreateTicketResponse createTicket(CreateTicketRequest request);

    Integer addFanToTicket(Integer ticketId, Integer userId);
}
