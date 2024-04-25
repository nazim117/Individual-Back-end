package org.example.individualbackend.externalAPI;

import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class FootballAPITest {
    @Mock
    private MatchRepo matchRepo;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void getMatches_RetrievesDataSuccessfully(){
        //Arrange
        //Act
        FootballAPI footballAPI = new FootballAPI(matchRepo);
        FootballAPI footballAPIMock = Mockito.spy(footballAPI);
        List<MatchEntity> result = createMockMatchEntries();
        Mockito.when(footballAPIMock.getMatchesData()).thenReturn(result);

        //Assert
        assertNotEquals(0,result.size());
    }

    @Test
     void getMatches_FailsToRetrieveData(){
        //Arrange
        //Act
        FootballAPI footballAPI = new FootballAPI(matchRepo);
        FootballAPI footballAPIMock = Mockito.spy(footballAPI);
        Mockito.when(footballAPIMock.fetchMatchesData()).thenThrow(new RuntimeException("Error fetching match data"));
        //Assert

        assertThrows(ResponseStatusException.class, () -> footballAPIMock.getMatchesData());
    }

    @Test
     void getMatches_PopulatesMatchRepoWithData(){
        //Arrange
        FootballAPI footballAPI = new FootballAPI(matchRepo);
        FootballAPI footballAPIMock = Mockito.spy(footballAPI);
        List<MatchEntity> mockMatches= createMockMatchEntries();
        Mockito.when(footballAPIMock.fetchMatchesData()).thenReturn(mockMatches);

        //Act
        footballAPIMock.getMatchesData();

        //Assert
        verify(matchRepo, times(1)).deleteAll();
        verify(matchRepo, times(1)).saveAll(mockMatches);
    }

    @Test
     void getMatches_returnsEmptyListWhenFetchFails(){
        //Arrange
        FootballAPI footballAPI = new FootballAPI(matchRepo);
        FootballAPI footballAPIMock = Mockito.spy(footballAPI);
        Mockito.when(footballAPIMock.fetchMatchesData()).thenThrow(new RuntimeException());

        //Act

        //Assert
        assertThrows(ResponseStatusException.class, () -> footballAPIMock.getMatchesData());
    }

    private List<MatchEntity> createMockMatchEntries() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate1 = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        LocalDateTime matchDate2 = LocalDateTime.parse("2024-03-13T21:00:00", formatter);

        List<MatchEntity> mockMatches = new ArrayList<>();
        mockMatches.add(MatchEntity.builder()
                        .date(matchDate1)
                        .venueName("Old Trafford")
                        .statusShort("FN")
                        .homeTeamName("Manchester United")
                        .homeTeamLogo("logo1.png")
                        .homeTeamWinner(false)
                        .awayTeamName("Liverpool")
                        .awayTeamLogo("logo2.png")
                        .awayTeamWinner(true)
                        .goalsHome(2)
                        .goalsAway(3)
                        .build());

        mockMatches.add(MatchEntity.builder()
                .date(matchDate2)
                .venueName("Anfield")
                .statusShort("FN")
                .homeTeamName("Liverpool")
                .homeTeamLogo("lvlogo.png")
                .homeTeamWinner(true)
                .awayTeamName("Arsenal")
                .awayTeamLogo("arslogo.png")
                .awayTeamWinner(false)
                .goalsHome(4)
                .goalsAway(1)
                .build());

        return mockMatches;
    }
}