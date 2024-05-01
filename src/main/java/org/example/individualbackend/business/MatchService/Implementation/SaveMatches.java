package org.example.individualbackend.business.MatchService.Implementation;

import lombok.AllArgsConstructor;
import org.example.individualbackend.externalAPI.FootballAPI;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
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

    public List<MatchEntity> getMatchesData(){
        try {
            List<MatchEntity> matchEntityList = footballAPI.fetchMatchesData();
            if(matchEntityList.isEmpty()) return new ArrayList<>();

            //matchRepo.deleteAll();

            if(matchRepo.getMatchEntitiesBy().size() <= 3){
                matchRepo.saveAll(matchEntityList);
            }

            return matchEntityList;

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error fetching match data");
        }

    }
}
