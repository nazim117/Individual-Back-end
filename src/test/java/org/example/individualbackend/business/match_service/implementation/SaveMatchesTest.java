package org.example.individualbackend.business.match_service.implementation;

import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.external_api.FootballAPI;
import org.example.individualbackend.external_api.UnirestWrapper;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.utilities.TicketGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class SaveMatchesTest {
    @Mock
    private MatchRepo matchRepo;
    @Mock
    private TicketRepo ticketRepo;
    @Mock
    private FootballAPI footballAPI;
    @InjectMocks
    private SaveMatches saveMatches;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMatchesData_Desc_Success() throws IOException {
        List<MatchEntity> mockMatchEntities = new ArrayList<>();
        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);

        List<MatchEntity> result = saveMatches.getMatchesDataDescDate();

        assertEquals(mockMatchEntities, result);
        verify(matchRepo, times(2)).findAllByOrderByDateDesc();
    }

    @Test
    void getMatchesData_SaveMatchesWhenRepoIsEmptyDesc() throws IOException {
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();
        List<TicketEntity> mockTicketEntities = TicketGenerator.generateTickets(2000);

        when(matchRepo.findAllByOrderByDateDesc())
                .thenReturn(new ArrayList<>())
                .thenReturn(mockMatchEntities);
        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);

        List<MatchEntity> result = saveMatches.getMatchesDataDescDate();

        verify(matchRepo, times(1)).saveAll(mockMatchEntities);
        verify(ticketRepo, times(mockMatchEntities.size() * mockTicketEntities.size())).save(any(TicketEntity.class));

        assertEquals(mockMatchEntities.size(), result.size());
        assertTrue(result.containsAll(mockMatchEntities));
    }

    @Test
    void getMatchesData_DoNotSaveMatchesWhenRepoHasEnoughDataDesc() throws IOException {
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();
        List<TicketEntity> mockTicketEntities = TicketGenerator.generateTickets(2000);

        mockMatchEntities.add(createMatchEntity(4, LocalDateTime.now().plusDays(1), "Anfield", 2000, "FT", "Real Madrid", "realmadrid.png", false, "Manchester City", "manchity.png", true, 2 ,5 , mockTicketEntities));
        mockMatchEntities.add(createMatchEntity(5, LocalDateTime.now().plusDays(2), "Anfield", 2000, "FT", "Real Sociedad", "realsociedad.png", false, "Manchester City", "manchity.png", true, 2 ,5 , mockTicketEntities));

        when(matchRepo.findAllByOrderByDateDesc()).thenReturn(mockMatchEntities);
        when(footballAPI.fetchMatchesData()).thenReturn(mockMatchEntities);

        List<MatchEntity> result = saveMatches.getMatchesDataDescDate();

        assertEquals(mockMatchEntities, result);
        verify(matchRepo, never()).saveAll(anyList());
    }

    @Test
    void getMatchesData_Desc_ThrowsExceptionWhenAPIFails() throws IOException {
        when(footballAPI.fetchMatchesData()).thenThrow(new IOException());

        assertThrows(ResponseStatusException.class, () -> saveMatches.getMatchesDataDescDate());
    }

    @Test
    void getTop6MatchesData_Success() {
        //Arrange
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();
        when(matchRepo.find6UpcomingMatches()).thenReturn(mockMatchEntities);

        //Act
        List<MatchEntity> result = saveMatches.getTop6MatchesData();

        //Assert
        assertEquals(3, result.size());
        assertEquals(mockMatchEntities.subList(0,3), result);
    }

    @Test
    void getTop3MatchesData_LessThan6Matches(){
        //Arrange
        List<MatchEntity> mockMatchEntities = createMockMatchEntityList();
        when(matchRepo.find6UpcomingMatches()).thenReturn(mockMatchEntities.subList(0,2));

        //Act
        List<MatchEntity> result = saveMatches.getTop6MatchesData();

        //Assert
        assertEquals(2, result.size());
        assertEquals(mockMatchEntities.subList(0,2), result);
    }

    @Test
    void getTop6MatchesData_NoMatches(){
        //Arrange
        when(matchRepo.find6UpcomingMatches()).thenReturn(new ArrayList<>());

        //Act
        List<MatchEntity> result = saveMatches.getTop6MatchesData();

        //Assert
        assertEquals(0, result.size());
    }

    @Test
    void getTop6MatchesData_ThrowsException(){
        when(matchRepo.find6UpcomingMatches()).thenThrow(new RuntimeException("Error"));

        assertThrows(ResponseStatusException.class, () -> saveMatches.getTop6MatchesData());
    }

    @Test
    void getMatchesData_Desc_FutureDate_GenerateTickets() throws IOException {
        //Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<TicketEntity> mockTicketEntities = TicketGenerator.generateTickets(2000);
        MatchEntity match = createMatchEntity(1, LocalDateTime.now().plusDays(1), "Turf Moor", 2000,"FT", "Burnley", "https://media.api-sports.io/football/teams/44.png", false, "Manchester City", "https://media.api-sports.io/football/teams/50.png", true, 0 ,3 , mockTicketEntities);

        when(footballAPI.fetchMatchesData()).thenReturn(Collections.singletonList(match));
        when(matchRepo.findAllByOrderByDateDesc()).thenReturn(Collections.singletonList(match));

        //Act
        List<MatchEntity> result = saveMatches.getMatchesDataDescDate();

        //Assert
        assertEquals(1, result.size());
        MatchEntity savedMatch = result.get(0);
        assertNotNull(savedMatch.getAvailableTickets());
        assertFalse(savedMatch.getAvailableTickets().isEmpty());
        assertEquals(match, savedMatch);
    }

    @Test
    void getMatchesData_Desc_PastDate_NoTicketsGenerated() throws IOException {
        //Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        MatchEntity match = createMatchEntity(1, LocalDateTime.now().minusDays(1), "Turf Moor", 2000,"FT", "Burnley", "https://media.api-sports.io/football/teams/44.png", false, "Manchester City", "https://media.api-sports.io/football/teams/50.png", true, 0 ,3 , null);

        when(footballAPI.fetchMatchesData()).thenReturn(Collections.singletonList(match));
        when(saveMatches.getMatchesDataDescDate()).thenReturn(Collections.singletonList(match));
        //Act
        List<MatchEntity> result = saveMatches.getMatchesDataDescDate();

        //Assert
        assertEquals(1, result.size());
        MatchEntity savedMatch = result.get(0);
        assertNull(savedMatch.getAvailableTickets());
        assertEquals(match, savedMatch);
    }

    @Test
    void getMatchesData_Desc_errorSavingTickets_ThrowsException() throws IOException {
        //Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        MatchEntity match = createMatchEntity(1, LocalDateTime.now().plusDays(1), "Turf Moor", 2000, "FT", "Burnley", "https://media.api-sports.io/football/teams/44.png", false, "Manchester City", "https://media.api-sports.io/football/teams/50.png", true, 0 ,3 , null);

        when(footballAPI.fetchMatchesData()).thenReturn(Collections.singletonList(match));
        when(ticketRepo.save(any(TicketEntity.class))).thenThrow(new RuntimeException("Error saving tickets"));

        //Act
        //Assert
        assertThrows(ResponseStatusException.class, () -> saveMatches.getMatchesDataDescDate());

    }

    private List<MatchEntity> createMockMatchEntityList() {
        List<MatchEntity> mockMatchEntities = new ArrayList<>();
        List<TicketEntity> mockTicketEntities = TicketGenerator.generateTickets(2000);
        MatchEntity mockMatchEntity3 = createMatchEntity(1, LocalDateTime.now().plusDays(1), "Anfield", 2000, "FT", "Liverpool", "liverpool.png", false, "Manchester City", "manchity.png", true, 1 ,4 , mockTicketEntities);
        MatchEntity mockMatchEntity2 = createMatchEntity(2, LocalDateTime.now().plusDays(2), "Anfield", 2000, "FT", "Liverpool", "liverpool.png", false, "Manchester City", "manchity.png", true, 1 ,2 , mockTicketEntities);
        MatchEntity mockMatchEntity1 = createMatchEntity(3, LocalDateTime.now().plusDays(3), "Turf Moor", 2000, "FT", "Burnley", "https://media.api-sports.io/football/teams/44.png", false, "Manchester City", "https://media.api-sports.io/football/teams/50.png", true, 0 ,3 , mockTicketEntities);

        mockMatchEntities.add(mockMatchEntity1);
        mockMatchEntities.add(mockMatchEntity2);
        mockMatchEntities.add(mockMatchEntity3);

        return mockMatchEntities;
    }

    private MatchEntity createMatchEntity(int id, LocalDateTime date, String venueName, int venueCapacity, String statusShort, String homeTeamName, String homeTeamLogo, boolean homeTeamWinner, String awayTeamName, String awayTeamLogo, boolean awayTeamWinner, int goalsHomeTeam, int goalsAwayTeam, List<TicketEntity> availableTickets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

        return MatchEntity.builder()
                .id(id)
                .date(date)
                .venueName(venueName)
                .venueCapacity(venueCapacity)
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