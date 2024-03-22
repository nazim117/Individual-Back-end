package org.example.individualbackend.persistance.impl;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
@AllArgsConstructor
public class FakeMatchRepoImpl implements MatchRepo {
    private final List<MatchEntity> savedMatches;
    private  static Integer NEXT_ID = 1;
    @Override
    public List<MatchEntity> getAllMatches(String leagueId, String seasonId) {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://v3.football.api-sports.io/teams?league=39&season=2023")
                    .header("x-rapidapi-key", "bba8076986df76d4b9dcfe836bbd8f0c")
                    .asString();

            if(response.getCode() == 200){
            }else{

            }
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        return Collections.unmodifiableList(savedMatches);
    }

    @Override
    public MatchEntity findById(Integer id) {
        return savedMatches
                .stream()
                .filter(matchEntity -> Objects.equals(matchEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MatchEntity save(MatchEntity match) {
        match.setId(NEXT_ID);
        NEXT_ID++;
        savedMatches.add(match);
        return match;
    }

    @Override
    public int count() {
        return savedMatches.size();
    }
}
