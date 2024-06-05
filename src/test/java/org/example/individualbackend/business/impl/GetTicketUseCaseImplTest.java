package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.ticket_service.implementation.GetTicketUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetTicketUseCaseImplTest {
    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private GetTicketUseCaseImpl getTicketUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void getTickets_ValidId_ReturnsTicket(){
        Integer id = 1;
        TicketEntity ticketEntity = createMockTicket(1, 20.0, 5, 283, createFan(1), createMatchEntity(1,
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
                3));

        when(ticketRepo.getTicketEntityById(id)).thenReturn(ticketEntity);

        TicketEntity ticket = getTicketUseCase.getTicket(1);

        assertNotNull(ticket);
        assertEquals(id, ticket.getId());
    }

    @Test
     void getTicket_InvalidId_ReturnsNull(){
        Integer nonexistentId = 999;
        when(ticketRepo.getTicketEntityById(nonexistentId)).thenReturn(null);

        TicketEntity ticket = getTicketUseCase.getTicket(nonexistentId);

        assertEquals(null, ticket);
    }

    private TicketEntity createMockTicket(Integer id,
                                      Double price,
                                      Integer rowNum,
                                      Integer seatNum,
                                      FanEntity fanEntity,
                                      MatchEntity matchEntity){

        return TicketEntity.builder()
                .id(id)
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNum)
                .fan(fanEntity)
                .footballMatch(matchEntity)
                .build();

    }
    private FanEntity createFan(Integer id){
        return FanEntity.builder().id(id).build();
    }

    private MatchEntity createMatchEntity(Integer _id,
                                          String _date,
                                          String _venueName,
                                          String _statusShort,
                                          String  _homeTeamName,
                                          String _homeTeamLogo,
                                          Boolean  _homeTeamWinner,
                                          String _awayTeamName,
                                          String _awayTeamLogo,
                                          Boolean _awayTeamWinner,
                                          Integer _goalsHome,
                                          Integer _goalsAway) {

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