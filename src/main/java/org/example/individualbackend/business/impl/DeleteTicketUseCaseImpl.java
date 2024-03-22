package org.example.individualbackend.business.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.DeleteTicketUseCase;
import org.example.individualbackend.persistance.TicketRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteTicketUseCaseImpl implements DeleteTicketUseCase {
    private final TicketRepo ticketRepo;
    @Override
    public void deleteTicket(Integer id) {ticketRepo.delete(id);}
}
