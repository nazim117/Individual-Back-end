package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.TicketService.Implementation.UpdateTicketUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestConfig.class})
class UpdateTicketUseCaseImplTest {
    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private UpdateTicketUseCaseImpl updateTicketUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void updateTicket_ValidRequest_UpdatesTicketFields_ReturnsTrue(){
        //Arrange
        Integer ticketId = 1;

        UpdateTicketRequest request = createUpdateTicketRequest(ticketId);
        TicketEntity existingTicket = createMockTicketEntity(ticketId);

        //Act
        when(ticketRepo.getTicketEntityById(ticketId)).thenReturn(existingTicket);
        when(ticketRepo.save(existingTicket)).thenReturn(existingTicket);

        //Assert
        assertDoesNotThrow(() -> updateTicketUseCase.updateTicket(request));
        verify(ticketRepo).save(existingTicket);
        assertEquals(request.getId(), existingTicket.getId());
        assertEquals(request.getPrice(), existingTicket.getPrice());
        assertEquals(request.getRowNum(), existingTicket.getRowNum());
        assertEquals(request.getSeatNumber(), existingTicket.getSeatNumber());
        assertEquals(request.getFanId(), existingTicket.getFan().getId());
        assertEquals(request.getMatchId(), existingTicket.getFootballMatch().getId());
    }

    @Test
     void updateTicket_InvalidTicketId_ThrowsException(){
        //Arrange
        Integer invalidTicketId = 998;
        UpdateTicketRequest request = createUpdateTicketRequest(invalidTicketId);
 
        //Act
        when(ticketRepo.getTicketEntityById(invalidTicketId)).thenReturn(null);

        //Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> updateTicketUseCase.updateTicket(request));
        assertEquals("Ticket_ID_INVALID", exception.getMessage());
        verify(ticketRepo, never()).save(any());
    }

    private TicketEntity createMockTicketEntity(Integer ticketId) {
        return TicketEntity.builder()
                .id(ticketId)
                .price(30.0)
                .rowNum(2)
                .seatNumber(223)
                .fan(FanEntity.builder().id(1).build())
                .footballMatch(createMatchEntity(1,
                        "2023-08-11T19:00:00",
                        "Turf Moor",
                        "FT",
                        "Burnley",
                        "https://media.api-sports.io/football/teams/44.png",
                        false,
                        "Manchester City",
                        "https://media.api-sports.io/football/teams/50.png",
                        true,
                        0,
                        3))
                .build();
    }

    private UpdateTicketRequest createUpdateTicketRequest(Integer ticketId) {
        return UpdateTicketRequest.builder()
                .id(ticketId)
                .price(30.0)
                .rowNum(2)
                .seatNumber(223)
                .fanId(1)
                .matchId(1)
                .build();
    }

    private MatchEntity createMatchEntity(int _id,
                                          String _date,
                                          String _venueName,
                                          String _statusShort,
                                          String _homeTeamName,
                                          String _homeTeamLogo,
                                          boolean _homeTeamWinner,
                                          String _awayTeamName,
                                          String _awayTeamLogo,
                                          boolean _awayTeamWinner,
                                          int _goalsHome,
                                          int _goalsAway) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return MatchEntity.builder()
                .id(_id)
                .date(LocalDateTime.parse(_date, formatter))
                .venueName(_venueName)
                .statusShort(_statusShort)
                .homeTeamName(_homeTeamName)
                .homeTeamLogo(_homeTeamLogo)
                .homeTeamWinner(_homeTeamWinner)
                .awayTeamName(_awayTeamName)
                .awayTeamLogo(_awayTeamLogo)
                .awayTeamWinner(_awayTeamWinner)
                .goalsHome(_goalsHome)
                .goalsAway(_goalsAway)
                .build();
    }
}