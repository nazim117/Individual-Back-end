package org.example.individualbackend.business.match_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.utilities.TicketGenerator;
import org.example.individualbackend.external_api.FootballAPI;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SaveMatches {
    private final MatchRepo matchRepo;
    private final TicketRepo ticketRepo;
    private final FootballAPI footballAPI;
    private static final String ERROR_FETCHING_MATCH_DATA = "Error fetching match data";

    @Transactional
    public List<MatchEntity> getMatchesDataDescDate() {
        try {
            List<MatchEntity> matchEntityList = footballAPI.fetchMatchesData();

            for (MatchEntity newMatchEntity : matchEntityList) {
                Optional<MatchEntity> matchEntityOptional = matchRepo.findById(newMatchEntity.getId());
                if (matchEntityOptional.isPresent()) {
                    if(newMatchEntity.getDate().isBefore(LocalDateTime.now())) {
                        MatchEntity matchEntity = matchEntityOptional.get();
                        matchEntity.setStatusShort(newMatchEntity.getStatusShort());
                        matchEntity.setGoalsHome(newMatchEntity.getGoalsHome());
                        matchEntity.setGoalsAway(newMatchEntity.getGoalsAway());
                        matchEntity.setHomeTeamWinner(newMatchEntity.getHomeTeamWinner());
                        matchEntity.setAwayTeamWinner(newMatchEntity.getAwayTeamWinner());
                    }else if(newMatchEntity.getAvailableTickets() == null){
                        List<TicketEntity> ticketEntityList = TicketGenerator.generateTickets(newMatchEntity.getVenueCapacity());
                        for(TicketEntity ticketEntity : ticketEntityList){
                            ticketEntity.setFootballMatch(newMatchEntity);
                            ticketRepo.save(ticketEntity);
                        }
                        if(ticketEntityList.isEmpty()){
                            newMatchEntity.setAvailableTickets(new ArrayList<>());
                        }else{
                            newMatchEntity.setAvailableTickets(ticketEntityList);
                        }
                    }
                }
            }
            return matchRepo.findAllByOrderByDateDesc();

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_MATCH_DATA);
        }
    }

    public List<MatchEntity> getTop6MatchesData(){
        try {
            List<MatchEntity> matchEntityList = matchRepo.find6UpcomingMatches();
            return matchEntityList.subList(0, Math.min(matchEntityList.size(), 6));
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_MATCH_DATA);
        }
    }

    public List<MatchEntity> getMatchesAscDate(){
        try{
            return matchRepo.findAllByOrderByDateAsc();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_MATCH_DATA);
        }
    }

    public List<MatchEntity> getMatchesByMostSoldTickets() {
        try{
            return matchRepo.findAllByMostSoldTickets();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_FETCHING_MATCH_DATA);
        }
    }
}