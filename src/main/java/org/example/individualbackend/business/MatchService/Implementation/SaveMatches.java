package org.example.individualbackend.business.MatchService.Implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.example.individualbackend.externalAPI.FootballAPI;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SaveMatches {
    private final MatchRepo matchRepo;
    private final FootballAPI footballAPI;

    @Autowired
    private EntityManager entityManager;

    public List<MatchEntity> getMatchesData(){
        try {
            List<MatchEntity> matchEntityList = footballAPI.fetchMatchesData();

            //matchRepo.deleteAll();

            if(matchRepo.findAllByOrderByDateAsc().size() <= 3){
                matchRepo.saveAll(matchEntityList);
            }else{
                matchEntityList = matchRepo.findAllByOrderByDateAsc();
            }

            if(matchEntityList.isEmpty()) return new ArrayList<>();

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