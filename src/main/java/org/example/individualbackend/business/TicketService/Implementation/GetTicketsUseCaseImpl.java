package org.example.individualbackend.business.TicketService.Implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.TicketService.Interface.GetTicketsUseCase;
import org.example.individualbackend.business.TicketService.Utilities.TicketConverter;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetTicketsUseCaseImpl implements GetTicketsUseCase {
    private final TicketRepo ticketRepo;
    private final FanRepo fanRepo;
    private final UserRepo userRepo;
    @Transactional
    @Override
    public GetAllTicketsResponse getAll() {
        List<Ticket> tickets = ticketRepo.findAll()
                .stream()
                .map(TicketConverter::convert)
                .toList();

        return GetAllTicketsResponse.builder()
                .tickets(tickets)
                .build();
    }
    @Transactional
    @Override
    public List<TicketEntity> getByMatchId(Integer matchId) {
        return ticketRepo.findByFootballMatchId(matchId);
    }

    @Transactional
    @Override
    public List<TicketEntity> getByFanId(int userId) {
        int fanId = userRepo.findById(userId).get().getFan().getId();

        if(fanId <= 0){
            throw new EntityNotFoundException("Fan does not exist");
        }
        return ticketRepo.findByFan_Id(fanId);
    }
}

