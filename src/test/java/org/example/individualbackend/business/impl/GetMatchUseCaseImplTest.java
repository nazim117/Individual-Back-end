package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.match_service.implementation.GetMatchUseCaseImpl;
import org.example.individualbackend.business.match_service.utilities.MatchConverter;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.match.Match;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
     void getMatch_ReturnsMatch(){
        Integer matchId = 1;
        MatchEntity expectedMatchEntity = createMatchEntity(matchId);
        Match expectedMatch = MatchConverter.convert(expectedMatchEntity);

        when(matchRepo.getMatchEntityById(matchId)).thenReturn(expectedMatchEntity);

        Match actualMatch = getMatchUseCase.getMatch(matchId);

        assertEquals(expectedMatch, actualMatch);
    }

    @Test
     void getMatch_Returns404NotFound(){
        Integer matchId = 1;

        when(matchRepo.getMatchEntityById(matchId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> getMatchUseCase.getMatch(matchId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
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
                .availableTickets(new ArrayList<>())
                .build();
    }
}