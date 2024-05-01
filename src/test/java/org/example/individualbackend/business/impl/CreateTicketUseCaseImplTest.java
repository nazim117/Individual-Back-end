package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.TicketService.Implementation.CreateTicketUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestConfig.class})
class CreateTicketUseCaseImplTest {
    @Mock
    private TicketRepo ticketRepo;
    @Mock
    private FanRepo fanRepo;
    @Mock
    private MatchRepo matchRepo;
    @InjectMocks
    private CreateTicketUseCaseImpl createTicketUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void createTicket_ValidTicketRequest_ReturnsResponse() {
        CreateTicketRequest createTicketRequest = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fanId(1)
                .footballMatchId(1)
                .build();

        TicketEntity ticketEntity = TicketEntity.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fan(createFanEntity(1))
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

        //createFanEntity();
        when(ticketRepo.existsByRowNumAndSeatNumber(anyInt(), anyInt())).thenReturn(false);
        when(fanRepo.findById(anyInt())).thenReturn(createFanEntity());
        when(matchRepo.getMatchEntityById(anyInt())).thenReturn(createMatchEntity(1,
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
        when(ticketRepo.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        CreateTicketResponse createTicketResponse = createTicketUseCase.createTicket(createTicketRequest);

        assertNotNull(createTicketResponse);
    }

    @Test
     void createTicket_TicketAlreadyExists_ThrowsException() {
        CreateTicketRequest createTicketRequest = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fanId(1)
                .footballMatchId(1)
                .build();

        when(ticketRepo.existsByRowNumAndSeatNumber(anyInt(), anyInt())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> createTicketUseCase.createTicket(createTicketRequest));
    }

    private FanEntity createFanEntity(){
        return FanEntity.builder().id(1).boughtTickets(null).build();
    }

    private FanEntity createFanEntity(Integer _id) {
        return FanEntity.builder()
                .id(_id)
                .build();
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