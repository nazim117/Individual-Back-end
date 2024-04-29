package org.example.individualbackend.business.impl;

import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetMatchUseCaseImplTest {
    @Mock
    private MatchRepo matchRepo;
    @InjectMocks
    private GetMatchUseCaseImpl getMatchUseCase;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void getMatch_ReturnsMatchEntity(){
        Integer matchId = 1;
        MatchEntity expectedMatchEntity = createMatchEntity(matchId);

        when(matchRepo.getMatchEntityById(matchId)).thenReturn(expectedMatchEntity);

        MatchEntity actualMatchEntity = getMatchUseCase.getMatch(matchId);

        assertEquals(expectedMatchEntity, actualMatchEntity);
    }

    @Test
     void getMatch_ReturnsEmptyMatchEntity(){
        Integer matchId = 1;

        when(matchRepo.getMatchEntityById(matchId)).thenReturn(null);

        MatchEntity actualMatchEntity = getMatchUseCase.getMatch(matchId);

        assertNull(actualMatchEntity);
    }

    private MatchEntity createMatchEntity(Integer matchId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return MatchEntity.builder()
                .id(matchId)
                .date(LocalDateTime.parse("2023-08-11T19:00:00", formatter))
                .venueName("Turf Moor")
                .statusShort("FT")
                .homeTeamName("Burnley")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(0)
                .goalsAway(3)
                .build();
    }
}