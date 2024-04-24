package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.GetTicketsUseCase;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class GetTicketsUseCaseImplTest {
    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private GetTicketsUseCaseImpl getTicketsUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTickets_SuccessfullyReceiveTickets(){
        //Arrange
        List<TicketEntity> mockTickets = createTicketList();
        when(ticketRepo.getTicketEntitiesBy()).thenReturn(mockTickets);

        List<Ticket> tickets = mockTickets
                .stream()
                .map(TicketConverter::convert)
                .toList();


        //Act
        GetAllTicketsResponse response = getTicketsUseCase.getTickets();

        //Assert
        assertEquals(tickets.size(), response.getTickets().size());
        assertEquals(tickets, response.getTickets());

    }

    @Test
    public void getTickets_NoTicketsExist_ReturnsEmptyList(){
        when(ticketRepo.getTicketEntitiesBy()).thenReturn(new ArrayList<>());

        GetAllTicketsResponse response = getTicketsUseCase.getTickets();

        assertEquals(0, response.getTickets().size());
    }

    private List<TicketEntity> createTicketList() {
        List<TicketEntity> tickets = new ArrayList<>();
        tickets.add(createTicket(1, 20.0, 5, 283, createFan(1), createMatchEntity(1,
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
                3)));

        tickets.add(createTicket(2, 50.0, 1, 45, createFan(2), createMatchEntity(2,
                "2023-09-11T19:00:00",
                "Anfield",
                "FT",
                "Liverpool",
                "https://media.api-sports.io/football/teams/44.png",
                false,
                "Manchester City",
                "https://media.api-sports.io/football/teams/50.png",
                true,
                2,
                3)));

        return tickets;
    }

    private TicketEntity createTicket(Integer id,
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