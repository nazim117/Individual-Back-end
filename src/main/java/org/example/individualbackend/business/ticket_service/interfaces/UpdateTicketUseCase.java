package org.example.individualbackend.business.ticket_service.interfaces;

import org.example.individualbackend.domain.update.UpdateTicketRequest;

public interface UpdateTicketUseCase {
    void updateTicket(UpdateTicketRequest request);
}
