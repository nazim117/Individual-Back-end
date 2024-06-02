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
public class FootballAPITest {

    @InjectMocks
    private FootballAPI footballAPI;

    @Mock
    private UnirestWrapper unirestWrapper;

    @Mock
    private HttpResponse<String> httpResponse;

    @Test
    void testFetchMatchesData() throws IOException {
        final String jsonResponse = "{ \"response\": [{\"fixture\": {\"id\": 1, \"date\": \"2024-04-01T15:00:00+00:00\", \"venue\": {\"name\": \"Stadium\"}, \"status\": {\"short\": \"FT\"}}, \"teams\": {\"home\": {\"name\": \"HomeTeam\", \"logo\": \"homeLogo\", \"winner\": true}, \"away\": {\"name\": \"AwayTeam\", \"logo\": \"awayLogo\", \"winner\": false}}, \"goals\": {\"home\": 2, \"away\": 1}}]}";
        when(unirestWrapper.get(anyString())).thenReturn(httpResponse);
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getBody()).thenReturn(jsonResponse);

        List<MatchEntity> matches = footballAPI.fetchMatchesData();
        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        MatchEntity match = matches.get(0);
        assertEquals(1, match.getId());
        assertEquals(LocalDateTime.of(2024, 4, 1, 15, 0), match.getDate());
        assertEquals("Stadium", match.getVenueName());
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
