package org.example.individualbackend.business.TicketService.Interface;

import org.example.individualbackend.domain.update.UpdateTicketRequest;

public interface UpdateTicketUseCase {
    void updateTicket(UpdateTicketRequest request);
}
