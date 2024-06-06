package org.example.individualbackend.external_api;

import kong.unirest.HttpResponse;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballAPITest {

    @InjectMocks
    private FootballAPI footballAPI;

    @Mock
    private UnirestWrapper unirestWrapper;

    @Mock
    private HttpResponse<String> matchResponse;

    @Mock
    private HttpResponse<String> venueResponse;

    @Test
    void testFetchMatchesData() throws IOException {
        final String jsonResponse = "{ \"response\": [{\"fixture\": {\"id\": 1, \"date\": \"2024-04-01T15:00:00+00:00\", \"venue\": {\"name\": \"Stadium\"}, \"status\": {\"short\": \"FT\"}}, \"teams\": {\"home\": {\"name\": \"HomeTeam\", \"logo\": \"homeLogo\", \"winner\": true}, \"away\": {\"name\": \"AwayTeam\", \"logo\": \"awayLogo\", \"winner\": false}}, \"goals\": {\"home\": 2, \"away\": 1}}]}";
        final String jsonVenueResponse = """
                { "response": [{
                            "id": 694,
                            "name": "Stadium",
                            "address": "Olympischer Platz 3",
                            "city": "Berlin",
                            "country": "Germany",
                            "capacity": 2000,
                            "surface": "grass",
                            "image": "https://media.api-sports.io/football/venues/694.png"
                        }]}""";
        when(unirestWrapper.get("https://v3.football.api-sports.io/venues?country=Germany")).thenReturn(venueResponse);
        when(venueResponse.getStatus()).thenReturn(200);
        when(venueResponse.getBody()).thenReturn(jsonVenueResponse);

        when(unirestWrapper.get("https://v3.football.api-sports.io/fixtures?league=4&season=2024")).thenReturn(matchResponse);
        when(matchResponse.getStatus()).thenReturn(200);
        when(matchResponse.getBody()).thenReturn(jsonResponse);

        List<MatchEntity> matches = footballAPI.fetchMatchesData();

        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        MatchEntity match = matches.get(0);
        assertEquals(1, match.getId());
        assertEquals(LocalDateTime.of(2024, 4, 1, 15, 0), match.getDate());
        assertEquals("Stadium", match.getVenueName());
        assertEquals(2000, match.getVenueCapacity());
        assertEquals("FT", match.getStatusShort());
        assertEquals("HomeTeam", match.getHomeTeamName());
        assertEquals("homeLogo", match.getHomeTeamLogo());
        assertTrue(match.getHomeTeamWinner());
        assertEquals("AwayTeam", match.getAwayTeamName());
        assertEquals("awayLogo", match.getAwayTeamLogo());
        assertFalse(match.getAwayTeamWinner());
        assertEquals(2, match.getGoalsHome());
        assertEquals(1, match.getGoalsAway());
    }
}
