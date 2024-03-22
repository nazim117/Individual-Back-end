package org.example.individualbackend.business.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetTicketUseCase;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetTicketUseCaseImpl implements GetTicketUseCase {
    private final TicketRepo ticketRepo;
    @Override
    public TicketEntity getTicket(Integer id) {
        return ticketRepo.findById(id);

    }
}
