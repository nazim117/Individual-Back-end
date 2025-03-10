package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.notifications_service.interfaces.NotificationsUseCase;
import org.example.individualbackend.business.ticket_service.implementation.CreateTicketUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.persistance.repositories.FanRepo;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
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
import java.util.NoSuchElementException;
import java.util.Optional;

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
    private UserRepo userRepo;
    @Mock
    private MatchRepo matchRepo;
    @Mock
    private NotificationsUseCase notificationsUseCase;

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

        when(ticketRepo.existsByRowNumAndSeatNumber(anyInt(), anyInt())).thenReturn(false);
        when(fanRepo.findById(anyInt())).thenReturn(createFanEntity(1));
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

    @Test
    void addFanToTicket_ValidIds_FanAddedToTicket(){
        //Arrange
        Integer ticketId = 1;
        Integer userId = 1;

        TicketEntity existingTicket = createTicketEntity(ticketId, 20.0, 5, 22);
        FanEntity existingFan = createFanEntity(userId);
        UserEntity existingUser = createUserEntity(existingFan);

        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(userRepo.getUserEntityById(userId)).thenReturn(existingUser);
        when(ticketRepo.save(any(TicketEntity.class))).thenReturn(existingTicket);

        //Act
        Integer result = createTicketUseCase.addFanToTicket(ticketId, userId);

        //Assert
        assertEquals(ticketId, result);
        assertEquals(existingFan, existingTicket.getFan());
        assertEquals(21.0, existingTicket.getPrice());
    }

    @Test
    void addFanToTicket_InvalidTicketId_ThrowsException(){
        Integer invalidTicketId = -1;
        Integer userId = 1;

        when(ticketRepo.findById(invalidTicketId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> createTicketUseCase.addFanToTicket(invalidTicketId, userId));
    }

    @Test
    void addFanToTicket_InvalidUserId_ThrowsException(){
        Integer ticketId = 1;
        Integer invalidUserId = -1;

        TicketEntity existingTicket = createTicketEntity(ticketId, 20.0, 5, 22);

        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(userRepo.getUserEntityById(invalidUserId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> createTicketUseCase.addFanToTicket(ticketId, invalidUserId));
    }

    private UserEntity createUserEntity(FanEntity existingFan) {
        return UserEntity.builder()
                .id(1)
                .email("validEmail@example.com")
                .fName("John")
                .lName("Doe")
                .password("validPassword")
                .fan(existingFan)
                .build();
    }

    private TicketEntity createTicketEntity(Integer id, double price, int rowNum, int seatNumber) {
        return TicketEntity.builder()
                .id(id)
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNumber).fan(createFanEntity(1))
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

    @Test
    void addFanToTicket_ValidTicketRequest_ReturnsNotNul(){
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
                .build();

        FanEntity fanEntity = createFanEntity(1);

        when(ticketRepo.existsByRowNumAndSeatNumber(anyInt(), anyInt())).thenReturn(false);
        when(fanRepo.findById(anyInt())).thenReturn(fanEntity);
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
        assertEquals(1, fanEntity.getId());
    }

    @Test
    void createTicket_ValidTicketRequest_FanNotInDb_ThrowsException(){
        //Arrange
        CreateTicketRequest createTicketRequest = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fanId(1)
                .footballMatchId(1)
                .build();

        when(fanRepo.findById(anyInt())).thenReturn(null);

        //Act
        //Assert
        assertThrows(ResponseStatusException.class, () -> createTicketUseCase.createTicket(createTicketRequest));
    }
    @Test
    void createTicket_TicketAlreadyExists_ThrowsExceptionWithSpecificMessage(){
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fanId(1)
                .footballMatchId(1)
                .build();

        when(ticketRepo.existsByRowNumAndSeatNumber(5,290)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> createTicketUseCase.createTicket(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Ticket already exists", exception.getReason());
    }
    @Test
    void addFanToTicket_FanDoesNotExist_ThrowsBadRequestException(){
        Integer ticketId = 1;
        Integer userId = 1;
        TicketEntity existingTicket = TicketEntity.builder().build();
        UserEntity employeeUser = UserEntity.builder()
                .id(userId)
                .fan(null)
                .build();

        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(userRepo.getUserEntityById(userId)).thenReturn(employeeUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> createTicketUseCase.addFanToTicket(ticketId,userId));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Fan does not exist", exception.getReason());
    }
    @Test
    void createTicket_ValidTicketRequest_MatchNotInDb_ThrowsException(){
        //Arrange
        CreateTicketRequest createTicketRequest = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(290)
                .fanId(1)
                .footballMatchId(1)
                .build();

        FanEntity fanEntity = createFanEntity(1);


        when(fanRepo.findById(anyInt())).thenReturn(fanEntity);
        when(matchRepo.getMatchEntityById(anyInt())).thenReturn(null);

        //Act
        //Assert
        assertThrows(ResponseStatusException.class, () -> createTicketUseCase.createTicket(createTicketRequest));
    }
}