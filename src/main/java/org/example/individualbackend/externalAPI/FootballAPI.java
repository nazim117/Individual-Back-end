package org.example.individualbackend.externalAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FootballAPI {
    private final MatchRepo matchRepo;

    public List<MatchEntity> fetchMatchesData() {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://v3.football.api-sports.io/fixtures?league=39&season=2023")
                    .header("x-rapidapi-key", "bba8076986df76d4b9dcfe836bbd8f0c")
                    .asString();

            if (response.getCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode fixtures = mapper.readTree(response.getBody()).get("response");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

                for (JsonNode fixtureNode : fixtures) {
                    matchEntityList.add(MatchEntity
                            .builder()
                            .id(fixtureNode.get("fixture").get("id").asInt())
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
            }

            return matchEntityList;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<MatchEntity> getMatchesData(){
        try {
            List<MatchEntity> matchEntityList = fetchMatchesData();
            if(matchEntityList.isEmpty()) return new ArrayList<>();

            matchRepo.deleteAll();
            matchRepo.saveAll(matchEntityList);

            return matchEntityList;

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error fetching match data");
        }

    }
}
