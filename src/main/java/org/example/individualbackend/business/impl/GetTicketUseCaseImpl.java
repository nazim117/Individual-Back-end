package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetTicketUseCase;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetTicketUseCaseImpl implements GetTicketUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public TicketEntity getTicket(Integer id) {
        return ticketRepo.getTicketEntityById(id);

    }
}
