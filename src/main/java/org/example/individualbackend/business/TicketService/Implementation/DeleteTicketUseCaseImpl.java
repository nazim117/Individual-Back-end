package org.example.individualbackend.business.TicketService.Implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.individualbackend.business.TicketService.Interface.DeleteTicketUseCase;
import org.example.individualbackend.persistance.TicketRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTicketUseCaseImpl implements DeleteTicketUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public void deleteTicket(Integer id) {ticketRepo.deleteById(id);}
}
