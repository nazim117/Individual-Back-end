package org.example.individualbackend.business.ticket_service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.ticket_service.interfaces.DeleteTicketUseCase;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTicketUseCaseImpl implements DeleteTicketUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public void deleteTicket(Integer id) {ticketRepo.deleteById(id);}
}
