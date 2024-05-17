package org.example.individualbackend.business.match_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.utilities.TicketGenerator;
import org.example.individualbackend.external_api.FootballAPI;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SaveMatches {
    private final MatchRepo matchRepo;
    private final TicketRepo ticketRepo;
    private final FootballAPI footballAPI;

    @Transactional
    public List<MatchEntity> getMatchesData() {
        try {
            List<MatchEntity> matchEntityList = matchRepo.findAllByOrderByDateAsc();
            if(matchEntityList.size() > 3) {
                return matchEntityList;
            }
            matchEntityList = footballAPI.fetchMatchesData();

            matchRepo.saveAll(matchEntityList);

            for(MatchEntity matchEntity : matchEntityList){
                if(matchEntity.getDate().isAfter(LocalDateTime.now())){
                    List<TicketEntity> ticketEntityList = TicketGenerator.generateTicket(2,5);
                    for(TicketEntity ticketEntity : ticketEntityList){
                        ticketEntity.setFootballMatch(matchEntity);
                        ticketRepo.save(ticketEntity);
                    }
                    matchEntity.setAvailableTickets(ticketEntityList);
                }
            }
            return matchEntityList;

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching match data");
        }

    }

    public List<MatchEntity> getTop3MatchesData(){
        try {
            List<MatchEntity> matchEntityList = matchRepo.find3UpcomingMatches();
            return matchEntityList.subList(0, Math.min(matchEntityList.size(), 3));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting match data");
        }
    }
}