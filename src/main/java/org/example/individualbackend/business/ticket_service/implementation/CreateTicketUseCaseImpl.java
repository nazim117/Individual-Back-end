package org.example.individualbackend.business.ticket_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.ticket_service.interfaces.CreateTicketUseCase;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CreateTicketUseCaseImpl implements CreateTicketUseCase {
    private final TicketRepo ticketRepo;
    private final FanRepo fanRepo;
    private final MatchRepo matchRepo;
    private final UserRepo userRepo;

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

    @Transactional
    @Override
    public Integer addFanToTicket(Integer ticketId, Integer userId) {
        TicketEntity existingTicket = ticketRepo
                .findById(ticketId)
                .orElseThrow();

        UserEntity existingUser =userRepo.getUserEntityById(userId);

        if(existingUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        FanEntity existingFan = existingUser.getFan();

        if(existingFan == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fan does not exist");
        }

        existingTicket.setFan(existingFan);

        return existingTicket.getId();
    }

    private TicketEntity addNewTicket(CreateTicketRequest request) {
        FanEntity fan = fanRepo.findById(request.getFanId());
        if(fan == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fan does not exist");
        }

        MatchEntity match = matchRepo.getMatchEntityById(request.getFootballMatchId());

        if(match == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Football match does not exist");
        }

        TicketEntity ticketEntity = TicketEntity.builder()
                .price(request.getPrice())
                .rowNum(request.getRowNum())
                .seatNumber(request.getSeatNumber())
                .fan(fan)
                .footballMatch(match)
                .build();

        return ticketRepo.save(ticketEntity);
    }
}
