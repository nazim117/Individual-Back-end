package org.example.individualbackend.business.impl;

import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.externalAPI.FootballAPI;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetMatchesUseCaseImplTest {
    @Mock
    private FootballAPI footballAPI;
    @InjectMocks
    private GetMatchesUseCaseImpl getMatchesUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_Matches_ReturnsAllMatches(){
        //Arrange
        List<MatchEntity> mockMatches = createMockMatches();
        when(footballAPI.getMatchesData()).thenReturn(mockMatches);

        List<Match> match = mockMatches.stream()
                .map(MatchConverter::convert)
                .toList();

        //Act
        GetAllMatchesResponse response = getMatchesUseCase.getMatches();

        //Assert
        assertEquals(match.size(), response.getMatches().size());
        assertEquals(match, response.getMatches());
    }

    private List<MatchEntity> createMockMatches() {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        matchEntityList.add(MatchEntity.builder()
                .id(1)
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
                .build());

        return matchEntityList;
    }

}