package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.CreateTicketUseCase;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CreateTicketUseCaseImpl implements CreateTicketUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public CreateTicketResponse createTicket(CreateTicketRequest request) {
        if(ticketRepo.existsByRowNumAndSeatNumber(request.getRowNum(), request.getSeatNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket already exists");
        }

        TicketEntity ticketEntity = addNewTicket(request);

        return CreateTicketResponse.builder()
                .id(ticketEntity.getId())
                .build();
    }

    private TicketEntity addNewTicket(CreateTicketRequest request) {
        TicketEntity ticketEntity = TicketEntity.builder()
                .price(request.getPrice())
                .rowNum(request.getRowNum())
                .seatNumber(request.getSeatNumber())
                .fan(FanEntity.builder().id(request.getFanId()).build())
                .footballMatch(MatchEntity.builder().id(request.getFootballMatchId()).build())
                .build();

        return ticketRepo.save(ticketEntity);
    }
}
