package org.example.individualbackend.business.MatchService.Implementation;

import org.example.individualbackend.business.MatchService.Interfaces.GetMatchesUseCase;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.externalAPI.FootballAPI;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class SaveMatchesTest {
    @Mock
    private MatchRepo matchRepo;
    @Mock
    private FootballAPI footballAPI;
    @InjectMocks
    private SaveMatches saveMatches;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMatchesData_Success(){
        List<MatchEntity> mockMatchEntities = new ArrayList<>();
        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);

        List<MatchEntity> result = saveMatches.getMatchesData();

        assertEquals(mockMatchEntities, result);
        verify(matchRepo, times(1)).saveAll(anyList());
    }

    @Test
    void getMatchesData_SaveMatchesWhenRepoIsEmpty(){
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();

        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);
        when(saveMatches.getMatchesData()).thenReturn(new ArrayList<>());

        List<MatchEntity> result = saveMatches.getMatchesData();

        assertEquals(mockMatchEntities, result);
        verify(matchRepo, times(1)).saveAll(mockMatchEntities);
    }

    @Test
    void getMatchesData_DoNotSaveMatchesWhenRepoHasEnoughData(){
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();
        mockMatchEntities.add(createMatchEntity(3, "2024-04-11T15:00:00", "Anfield", "FT", "Real Madrid", "realmadrid.png", false, "Manchester City", "manchity.png", true, 2 ,5 , 5));
        mockMatchEntities.add(createMatchEntity(4, "2024-04-13T15:00:00", "Anfield", "FT", "Real Sociedad", "realsociedad.png", false, "Manchester City", "manchity.png", true, 2 ,5 , 5));

        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);
        when(matchRepo.findAllByOrderByDateAsc()).thenReturn(mockMatchEntities);

        List<MatchEntity> result = saveMatches.getMatchesData();

        assertEquals(mockMatchEntities, result);
        verify(matchRepo, never()).saveAll(anyList());
    }

    @Test
    void getMatchesData_ThrowsExceptionWhenAPIFails() {
        when(footballAPI.fetchMatchesData()).thenThrow(new RuntimeException());

        assertThrows(ResponseStatusException.class, () -> saveMatches.getMatchesData());
    }

    private List<MatchEntity> createMockMatchEntityList() {
        List<MatchEntity> mockMatchEntities = new ArrayList<>();

        MatchEntity mockMatchEntity1 = createMatchEntity(1, "2023-08-11T19:00:00", "Turf Moor", "FT", "Burnley", "https://media.api-sports.io/football/teams/44.png", false, "Manchester City", "https://media.api-sports.io/football/teams/50.png", true, 0 ,3 , 5);
        MatchEntity mockMatchEntity2 = createMatchEntity(1, "2024-03-11T15:00:00", "Anfield", "FT", "Liverpool", "liverpool.png", false, "Manchester City", "manchity.png", true, 1 ,2 , 5);

        mockMatchEntities.add(mockMatchEntity1);
        mockMatchEntities.add(mockMatchEntity2);

        return mockMatchEntities;
    }

    private MatchEntity createMatchEntity(int id, String date, String venueName, String statusShort, String homeTeamName, String homeTeamLogo, boolean homeTeamWinner, String awayTeamName, String awayTeamLogo, boolean awayTeamWinner, int goalsHomeTeam, int goalsAwayTeam, int availableTickets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return MatchEntity.builder()
                .id(id)
                .date(LocalDateTime.parse(date, formatter))
                .venueName(venueName)
                .statusShort(statusShort)
                .homeTeamName(homeTeamName)
                .homeTeamLogo(homeTeamLogo)
                .homeTeamWinner(homeTeamWinner)
                .awayTeamName(awayTeamName)
                .awayTeamLogo(awayTeamLogo)
                .awayTeamWinner(awayTeamWinner)
                .goalsHome(goalsHomeTeam)
                .goalsAway(goalsAwayTeam)
                .availableTickets(availableTickets)
                .build();
    }

}