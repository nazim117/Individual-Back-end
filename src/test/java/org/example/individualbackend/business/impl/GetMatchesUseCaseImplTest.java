package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.match_service.implementation.GetMatchesUseCaseImpl;
import org.example.individualbackend.business.match_service.implementation.SaveMatches;
import org.example.individualbackend.business.match_service.utilities.MatchConverter;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.match.Match;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(saveMatches.getMatchesDataDescDate()).thenReturn(mockMatches);

        List<Match> matches = mockMatches.stream()
                .map(MatchConverter::convert)
                .toList();

        //Act
        GetMatchesResponse response = getMatchesUseCase.getMatchesDescDate();

        //Assert
        assertEquals(matches.size(), response.getMatches().size());
        assertEquals(matches, response.getMatches());
    }

    @Test
    void get_Matches_ReturnsEmptyMatchEntityArray(){
        when(saveMatches.getMatchesDataDescDate()).thenReturn(new ArrayList<>());

        GetMatchesResponse response = getMatchesUseCase.getMatchesDescDate();

        assertEquals(0, response.getMatches().size());
    }

    @Test
    void getTop6Matches_ReturnsTop6Matches(){
        List<MatchEntity> mockMatches = createMockMatches();
        when(saveMatches.getTop6MatchesData()).thenReturn(mockMatches);

        List<Match> matches = mockMatches.stream()
                .map(MatchConverter::convert)
                .toList();


        GetMatchesResponse response = getMatchesUseCase.getTop6Matches();

        assertEquals(matches.size(), response.getMatches().size());
        assertEquals(matches, response.getMatches());
    }

    @Test
    void getMatchesAscDate_ReturnsMatchesInAscendingOrder(){
        List<MatchEntity> mockMatches = createMockMatches();
        when(saveMatches.getMatchesAscDate()).thenReturn(mockMatches);

        GetMatchesResponse response = getMatchesUseCase.getMatchesAscDate();

        LocalDateTime previousDate = LocalDateTime.MIN;

        for(Match match : response.getMatches()){
            assertTrue(previousDate.isBefore(match.getDate()));
            previousDate = match.getDate();
        }
    }

    @Test
    void getMatchesDescDate_ReturnsMatchesInDescendingOrder(){
        List<MatchEntity> mockMatches = createMockMatchesDesc();
        when(saveMatches.getMatchesDataDescDate()).thenReturn(mockMatches);

        GetMatchesResponse response = getMatchesUseCase.getMatchesAscDate();

        LocalDateTime previousDate = LocalDateTime.MAX;
        for(Match match : response.getMatches()){
            assertTrue(previousDate.isAfter(match.getDate()));
            previousDate = match.getDate();
        }
    }

    @Test
    void getMatchesBySoldTickets_ReturnsMatchesSortedByTicketSales(){
        List<MatchEntity> mockMatches = createMockMatchesWithTicketSales();
        when(saveMatches.getMatchesByMostSoldTickets()).thenReturn(mockMatches);

        GetMatchesResponse response = getMatchesUseCase.getMatchesBySoldTickets();

        assertEquals(2, response.getMatches().size());
        assertTrue(response.getMatches().get(0).getAvailableTickets().size() > response.getMatches().get(1).getAvailableTickets().size());
    }

    @Test
    void getMatches_NoMatchesAvailable_ReturnsEmptyList(){
        when(saveMatches.getMatchesDataDescDate()).thenReturn(new ArrayList<>());

        GetMatchesResponse response = getMatchesUseCase.getMatchesDescDate();

        assertTrue(response.getMatches().isEmpty());
    }

    @Test
    void getMatches_SingleMatch_returnsSingleMatchList(){
        List<MatchEntity> singleMatchList = Collections.singletonList(createMockMatchEntity());
        when(saveMatches.getMatchesDataDescDate()).thenReturn(singleMatchList);

        GetMatchesResponse response = getMatchesUseCase.getMatchesDescDate();

        assertEquals(1, response.getMatches().size());
        assertEquals(singleMatchList.get(0).getId(), response.getMatches().get(0).getId());
    }

    private MatchEntity createMockMatchEntity() {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-12T19:00:00", formatter))
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

    private List<MatchEntity> createMockMatchesWithTicketSales() {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        MatchEntity match1 = MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-12T19:00:00", formatter))
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


        List<TicketEntity> tickets1 = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            tickets1.add(createMockTicket(i+1));
        }
        match1.setAvailableTickets(tickets1);

        MatchEntity match2 = MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-11T19:00:00", formatter))
                .venueName("Old Trafford")
                .statusShort("FT")
                .homeTeamName("Manchester United")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(2)
                .goalsAway(3)
                .availableTickets(new ArrayList<>())
                .build();

        List<TicketEntity> tickets2 = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            tickets2.add(createMockTicket(i+1));
        }
        match2.setAvailableTickets(tickets2);

        matchEntityList.add(match1);
        matchEntityList.add(match2);

        matchEntityList.sort((m1, m2) -> {
            int countComparison = Integer.compare(m2.getAvailableTickets().size(), m1.getAvailableTickets().size());
            if(countComparison == 0){
                return m1.getDate().compareTo(m2.getDate());
            }
            return countComparison;
        });

        return matchEntityList;
    }

    private TicketEntity createMockTicket(Integer fanId){
        FanEntity fan = FanEntity.builder().id(fanId).build();
        TicketEntity ticket = TicketEntity.builder().fan(fan).build();
        return ticket;
    }

    private List<MatchEntity> createMockMatchesDesc() {
        List<MatchEntity> matchEntityList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        matchEntityList.add(MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-12T19:00:00", formatter))
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
                .build());

        matchEntityList.add(MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-11T19:00:00", formatter))
                .venueName("Old Trafford")
                .statusShort("FT")
                .homeTeamName("Manchester United")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(2)
                .goalsAway(3)
                .availableTickets(new ArrayList<>())
                .build());

        return matchEntityList;
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
                .availableTickets(new ArrayList<>())
                .build());

        matchEntityList.add(MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-12T19:00:00", formatter))
                .venueName("Old Trafford")
                .statusShort("FT")
                .homeTeamName("Manchester United")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(2)
                .goalsAway(3)
                .availableTickets(new ArrayList<>())
                .build());

        return matchEntityList;
    }

}