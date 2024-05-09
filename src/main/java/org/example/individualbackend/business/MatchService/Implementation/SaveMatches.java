package org.example.individualbackend.business.MatchService.Implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.Utils.TicketGenerator;
import org.example.individualbackend.externalAPI.FootballAPI;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SaveMatches {
    private final MatchRepo matchRepo;
    private final TicketRepo ticketRepo;
    private final FootballAPI footballAPI;

    @Autowired
    private EntityManager entityManager;

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
                    List<TicketEntity> ticketEntityList = TicketGenerator.INSTANCE.generateTicket(2,5);
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
            TypedQuery<MatchEntity> query = entityManager.createQuery(
                    "SELECT m FROM MatchEntity m WHERE m.date >= CURRENT_TIMESTAMP ORDER BY m.date ASC", MatchEntity.class
            );
            query.setMaxResults(3);
            List<MatchEntity> matchEntityList = query.getResultList();
            return matchEntityList;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting match data");
        }
    }
}