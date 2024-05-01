package org.example.individualbackend.business.TicketService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.TicketService.Interface.GetTicketsUseCase;
import org.example.individualbackend.business.TicketService.Utilities.TicketConverter;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.persistance.TicketRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetTicketsUseCaseImpl implements GetTicketsUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public GetAllTicketsResponse getTickets() {
        List<Ticket> tickets = ticketRepo.findAll()
                .stream()
                .map(TicketConverter::convert)
                .toList();

        return GetAllTicketsResponse.builder()
                .tickets(tickets)
                .build();
    }
}

