package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.MatchService.Implementation.GetMatchesUseCaseImpl;
import org.example.individualbackend.business.MatchService.Implementation.SaveMatches;
import org.example.individualbackend.business.MatchService.Utilities.MatchConverter;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetMatchesUseCaseImplTest {
    @Mock
    private SaveMatches saveMatches;
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
        when(saveMatches.getMatchesData()).thenReturn(mockMatches);

        List<Match> match = mockMatches.stream()
                .map(MatchConverter::convert)
                .toList();

        //Act
        GetMatchesResponse response = getMatchesUseCase.getMatches();

        //Assert
        assertEquals(match.size(), response.getMatches().size());
        assertEquals(match, response.getMatches());
    }

    @Test
    public void get_Matches_ReturnsEmptyMatchEntityArray(){
        when(saveMatches.getMatchesData()).thenReturn(new ArrayList<>());

        GetMatchesResponse response = getMatchesUseCase.getMatches();

        assertEquals(0, response.getMatches().size());
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