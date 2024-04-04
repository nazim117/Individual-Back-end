package org.example.individualbackend.persistance.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
@AllArgsConstructor
public class FakeMatchRepoImpl implements MatchRepo {
    private final List<MatchEntity> savedMatches;
    @Override
    public List<MatchEntity> getAllMatches(String leagueId, String seasonId) {
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
    public boolean save() {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://v3.football.api-sports.io/fixtures?league=39&season=2023")
                    .header("x-rapidapi-key", "bba8076986df76d4b9dcfe836bbd8f0c")
                    .asString();

            if(response.getCode() == 200){
                ObjectMapper mapper = new ObjectMapper();
                JsonNode fixtures = mapper.readTree(response.getBody()).get("response");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

                for(JsonNode fixtureNode : fixtures){
                    savedMatches.add(MatchEntity
                            .builder()
                            .id(fixtureNode.get("fixture").get("id").asInt())
                            .timezone(fixtureNode.get("fixture").get("timezone").asText())
                            .date(LocalDateTime.parse(fixtureNode.get("fixture").get("date").asText(), formatter))
                            .venueName(fixtureNode.get("fixture").get("venue").get("name").asText())
                            .statusShort(fixtureNode.get("fixture").get("status").get("short").asText())
                            .homeTeamName(fixtureNode.get("teams").get("home").get("name").asText())
                            .homeTeamLogo(fixtureNode.get("teams").get("home").get("logo").asText())
                            .homeTeamWinner(fixtureNode.get("teams").get("home").get("winner").asBoolean())
                            .awayTeamName(fixtureNode.get("teams").get("away").get("name").asText())
                            .awayTeamLogo(fixtureNode.get("teams").get("away").get("logo").asText())
                            .awayTeamWinner(fixtureNode.get("teams").get("away").get("winner").asBoolean())
                            .goalsHome(fixtureNode.get("goals").get("home").asInt())
                            .goalsAway(fixtureNode.get("goals").get("away").asInt())
                            .build());
                }

                return true;
            }else{
                return false;
            }
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count() {
        return savedMatches.size();
    }
}
