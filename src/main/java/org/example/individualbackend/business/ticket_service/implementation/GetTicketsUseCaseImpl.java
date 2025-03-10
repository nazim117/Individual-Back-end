package org.example.individualbackend.business.ticket_service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.ticket_service.interfaces.GetTicketsUseCase;
import org.example.individualbackend.business.ticket_service.utilities.TicketConverter;
import org.example.individualbackend.domain.match.MatchRevenueData;
import org.example.individualbackend.domain.match.MatchTicketData;
import org.example.individualbackend.domain.ticket.Ticket;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.persistance.repositories.FanRepo;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
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
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        FanEntity fan = userEntity.getFan();

        if(fan == null){
            throw new EntityNotFoundException("Fan does not exist");
        }
        return ticketRepo.findByFan_Id(fan.getId());
    }

    @Override
    public Long getTotalTicketsSold() {
        return ticketRepo.countTotalTicketsSold();
    }

    @Override
    public Double getTotalRevenue() {
        return ticketRepo.sumTotalRevenue();
    }

    @Override
    public List<MatchTicketData> getTicketsPerMatch() {
        return ticketRepo.countTicketsPerMatch();
    }

    @Override
    public List<MatchRevenueData> getRevenuePerMatch() {
        return ticketRepo.sumRevenuesPerMatch();
    }
}

