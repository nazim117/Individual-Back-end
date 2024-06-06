package org.example.individualbackend.external_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FootballAPI {
    private final UnirestWrapper unirestWrapper;
    public List<MatchEntity> fetchMatchesData() throws IOException {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        Map<String, Integer> venueCapacities = fetchVenueCapacities();
        try {
            HttpResponse<String> response = unirestWrapper.get("https://v3.football.api-sports.io/fixtures?league=4&season=2024");

            if (response.getStatus() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode fixtures = mapper.readTree(response.getBody()).get("response");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

                for (JsonNode fixtureNode : fixtures) {
                    if(fixtureNode != null){
                        JsonNode fixture = fixtureNode.get("fixture");
                        JsonNode teams = fixtureNode.get("teams");
                        JsonNode goals = fixtureNode.get("goals");
                        if(fixture != null && teams != null && goals != null){
                            String venueName = fixture.get("venue").get("name").asText();
                            int venueCapacity = venueCapacities.getOrDefault(venueName, 0);
                            MatchEntity match =  MatchEntity
                                    .builder()
                                    .id(fixture.get("id").asInt())
                                    .date(LocalDateTime.parse(fixture.get("date").asText(), formatter))
                                    .venueName(venueName)
                                    .venueCapacity(venueCapacity)
                                    .statusShort(fixtureNode.get("fixture").get("status").get("short").asText())
                                    .homeTeamName(teams.get("home").get("name").asText())
                                    .homeTeamLogo(teams.get("home").get("logo").asText())
                                    .homeTeamWinner(teams.get("home").get("winner").asBoolean())
                                    .awayTeamName(teams.get("away").get("name").asText())
                                    .awayTeamLogo(teams.get("away").get("logo").asText())
                                    .awayTeamWinner(teams.get("away").get("winner").asBoolean())
                                    .goalsHome(goals.get("home").asInt())
                                    .goalsAway(goals.get("away").asInt())
                                    .availableTickets(null)
                                    .build();

                            matchEntityList.add(match);
                        }
                    }
                }
            }else{
                throw new IllegalStateException("Failed to fetch matches data. HTTP status code " + response.getStatus());
            }
            return matchEntityList;

        } catch(Exception e){
            throw new IOException("Error fetching matches data: ", e);
        }
    }

    private Map<String, Integer> fetchVenueCapacities() throws IOException {
        Map<String, Integer> venueCapacities = new HashMap<>();
        try{
            HttpResponse<String> response = unirestWrapper.get("https://v3.football.api-sports.io/venues?country=Germany");

            if(response.getStatus() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode venues = mapper.readTree(response.getBody()).get("response");

                for (JsonNode venueNode : venues) {
                    String venueName = venueNode.get("name").asText();
                    int capacity = venueNode.get("capacity").asInt();
                    venueCapacities.put(venueName, capacity);
                }
            }else {
                throw new IllegalStateException("Failed to fetch venue capacities: HTTP status code " + response.getStatus());
            }
        }catch(Exception e){
            throw new IOException("Error fetching capacities: ", e);
        }
        return venueCapacities;
    }
}
