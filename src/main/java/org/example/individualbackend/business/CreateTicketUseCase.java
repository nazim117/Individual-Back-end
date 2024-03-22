package org.example.individualbackend.business;

import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;

public interface CreateTicketUseCase {
    CreateTicketResponse createTicket(CreateTicketRequest request);
}
