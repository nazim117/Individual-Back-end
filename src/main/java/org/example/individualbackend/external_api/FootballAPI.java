package org.example.individualbackend.external_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FootballAPI {
    public List<MatchEntity> fetchMatchesData() throws IOException {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://v3.football.api-sports.io/fixtures?league=4&season=2024")
                    .header("x-rapidapi-key", "bba8076986df76d4b9dcfe836bbd8f0c")
                    .asString();

            if (response.getCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode fixtures = mapper.readTree(response.getBody()).get("response");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

                for (JsonNode fixtureNode : fixtures) {
                    String fixture = "fixture";
                    String teams = "teams";
                    MatchEntity match =  MatchEntity
                            .builder()
                            .id(fixtureNode.get(fixture).get("id").asInt())
                            .date(LocalDateTime.parse(fixtureNode.get(fixture).get("date").asText(), formatter))
                            .venueName(fixtureNode.get(fixture).get("venue").get("name").asText())
                            .statusShort(fixtureNode.get("fixture").get("status").get("short").asText())
                            .homeTeamName(fixtureNode.get(teams).get("home").get("name").asText())
                            .homeTeamLogo(fixtureNode.get(teams).get("home").get("logo").asText())
                            .homeTeamWinner(fixtureNode.get(teams).get("home").get("winner").asBoolean())
                            .awayTeamName(fixtureNode.get(teams).get("away").get("name").asText())
                            .awayTeamLogo(fixtureNode.get(teams).get("away").get("logo").asText())
                            .awayTeamWinner(fixtureNode.get(teams).get("away").get("winner").asBoolean())
                            .goalsHome(fixtureNode.get("goals").get("home").asInt())
                            .goalsAway(fixtureNode.get("goals").get("away").asInt())
                            .build();

                    matchEntityList.add(match);
                }
            }else{
                throw new IllegalStateException("Failed to fetch matches data. HTTP status code " + response.getCode());
            }
            return matchEntityList;

        } catch(Exception e){
            throw new IOException("Error fetching matches data: ", e);
        }
    }
}
