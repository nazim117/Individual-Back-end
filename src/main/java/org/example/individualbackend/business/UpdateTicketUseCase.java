package org.example.individualbackend.business;

import org.example.individualbackend.domain.update.UpdateTicketRequest;

public interface UpdateTicketUseCase {
    void updateTicket(UpdateTicketRequest request);
}
