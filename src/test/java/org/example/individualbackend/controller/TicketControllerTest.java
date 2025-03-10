package org.example.individualbackend.controller;

import org.example.individualbackend.utilities.TicketGenerator;
import org.example.individualbackend.business.ticket_service.interfaces.*;
import org.example.individualbackend.domain.ticket.Ticket;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.example.individualbackend.persistance.repositories.UserRepo;
import org.example.individualbackend.persistance.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetTicketsUseCase getTicketsUseCase;
    @MockBean
    private GetTicketUseCase getTicketUseCase;
    @MockBean
    private CreateTicketUseCase createTicketUseCase;
    @MockBean
    private UpdateTicketUseCase updateTicketUseCase;
    @MockBean
    private DeleteTicketUseCase deleteTicketUseCase;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private TicketRepo ticketRepo;

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void getTickets_ReturnsListOfTickets() throws Exception{
        GetAllTicketsResponse response = GetAllTicketsResponse.builder().tickets(createTicketList()).build();
        when(getTicketsUseCase.getAll()).thenReturn(response);

        mockMvc.perform(get("/api/tickets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {
                                  "tickets": [
                                      {
                                          "price": 20.0,
                                          "rowNum": 5,
                                          "seatNumber": 443,
                                          "fan": {
                                              "id": 1
                                          },
                                          "footballMatch": {
                                              "id": 1,
                                              "date": "2024-03-13T10:30:00",
                                              "venueName": "Turf Moor",
                                              "statusShort": "FT",
                                              "homeTeamName": "Burnley",
                                              "homeTeamLogo": "https://media.api-sports.io/football/teams/44.png",
                                              "homeTeamWinner": false,
                                              "awayTeamName": "Manchester City",
                                              "awayTeamLogo": "https://media.api-sports.io/football/teams/50.png",
                                              "awayTeamWinner": true,
                                              "goalsHome": 0,
                                              "goalsAway": 3
                                          }
                                      },
                                      {
                                          "price": 50.0,
                                          "rowNum": 2,
                                          "seatNumber": 32,
                                          "fan": {
                                            "id": 1
                                          },
                                          "footballMatch": {
                                              "id": 1,
                                              "date": "2024-03-13T10:30:00",
                                              "venueName": "Turf Moor",
                                              "statusShort": "FT",
                                              "homeTeamName": "Burnley",
                                              "homeTeamLogo": "https://media.api-sports.io/football/teams/44.png",
                                              "homeTeamWinner": false,
                                              "awayTeamName": "Manchester City",
                                              "awayTeamLogo": "https://media.api-sports.io/football/teams/50.png",
                                              "awayTeamWinner": true,
                                              "goalsHome": 0,
                                              "goalsAway": 3
                                          }
                                      }
                                  ]
                              }
                        """));
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void getTicket_WithInvalidId_ReturnsNotFound() throws Exception {
        int invalidTicketId = -1;

        when(getTicketUseCase.getTicket(invalidTicketId)).thenReturn(null);

        mockMvc.perform(get("/api/tickets/{id}", invalidTicketId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void createTicket_WithValidRequest_ReturnsBadRequest() throws Exception {
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void buyTicket_WithValidRequest_ReturnsBadRequest() throws Exception {
        Integer userId = 1;
        Integer invalidTicketId = -1;

        when(createTicketUseCase.addFanToTicket(invalidTicketId, userId))
                .thenThrow(new NoSuchElementException("Ticket does not exist"));

        mockMvc.perform(post("/api/tickets/buy-ticket/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTicketId.toString()))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void buyTicket_WithInvalidRequest_ReturnsBadRequest() throws Exception{
        Integer invalidUserId = -1;
        Integer invalidTicketId = -1;

        when(createTicketUseCase.addFanToTicket(invalidTicketId, invalidUserId))
                .thenThrow(new NoSuchElementException("Ticket does not exist"));


        mockMvc.perform(post("/api/tickets/buy-ticket/{userId}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTicketId.toString()))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void createTicket_WithInvalidRequest_ReturnsBadRequest() throws Exception{
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        when(createTicketUseCase.createTicket(request)).thenThrow(new RuntimeException("Invalid request"));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void updateTicket_WithInvalidRequest_ReturnsBadRequest() throws Exception{
        //Arrange
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        doThrow(new RuntimeException("Invalid request")).when(updateTicketUseCase).updateTicket(request);

        //Act
        //Assert
        mockMvc.perform(put("/api/tickets/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void getTicket_WithValidId_ReturnsTicket() throws Exception{
        Mockito.when(getTicketUseCase.getTicket(Mockito.anyInt())).thenReturn(createTicketEntity(1, 20.0, 5, 70));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tickets/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void createTicket_WithValidRequest_ReturnsCreated() throws Exception{
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(443)
                .fanId(1)
                .footballMatchId(1).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());

    }
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void updateTicket_WithValidRequest_Successful_ReturnsNoContent() throws Exception{
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .id(1)
                .price(20.0)
                .rowNum(5)
                .seatNumber(443)
                .fanId(1)
                .matchId(1).build();

        doNothing().when(updateTicketUseCase).updateTicket(any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/api/tickets/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void deleteTicket_ValidId_ReturnsNoContent() throws Exception {
        Integer ticketId = 1;

        doNothing().when(deleteTicketUseCase).deleteTicket(ticketId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/tickets/{id}", ticketId))
                .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void createTicker_WithInvalidRequest_ReturnsBadRequest() throws Exception {
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void buyTicket_WithInvalidTicketId_ReturnsBadRequest() throws Exception {
        Integer userId = 1;
        Integer ticketId = -1;

        CreateTicketRequest request = CreateTicketRequest.builder()
                .fanId(ticketId).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tickets/buy-ticket/{id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void buyTicket_WithInvalidUserId_ReturnsBadRequest() throws Exception {
        Integer userId = -1;
        Integer ticketId = 1;

        CreateTicketRequest request = CreateTicketRequest.builder()
                .fanId(ticketId).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tickets/buy-ticket/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void buyTicket_WithValidRequest_ReturnsCreated() throws Exception {
        Integer userId = 1;
        Integer ticketId = 1;

        // Stubbing necessary data
        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .email("testemail@example.com")
                .fName("John")
                .lName("Doe")
                .password("passwrod2223")
                .fan(FanEntity.builder().id(1).build())
                .userRoles(Collections.singleton(UserRoleEntity.builder().role(RoleEnum.FOOTBALL_FAN).build()))
                .build();

        TicketEntity ticketEntity = createTicketEntity(1,20.0, 3, 44);

        when(userRepo.getUserEntityById(userId)).thenReturn(userEntity);
        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(ticketEntity));

        when(createTicketUseCase.addFanToTicket(ticketId, userId)).thenReturn(ticketId);

        mockMvc.perform(post("/api/tickets/buy-ticket/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketId.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(ticketId)));
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void getTicketByMatchId_WithEmptyList_ReturnsEmptyResponse() throws Exception {
        when(getTicketsUseCase.getByMatchId(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tickets/matches/{matchId}", 123))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void updateTicket_UnauthorizedRole_ReturnsForbidden() throws Exception {
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .id(1)
                .price(30.0)
                .rowNum(5)
                .seatNumber(15)
                .fanId(1)
                .matchId(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/tickets/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void createTicket_WithNegativePrice_ReturnsBadRequest() throws Exception {
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(-20.0)
                .rowNum(5)
                .seatNumber(15)
                .fanId(1)
                .footballMatchId(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private List<Ticket> createTicketList() {
        List<Ticket> tickets =  new ArrayList<>();

        tickets.add(createTicket(20.0,5, 443));
        tickets.add(createTicket(50.0,2, 32));

        return tickets;
    }
    private Ticket createTicket(Double price, int rowNum, int seatNumber) {
        return Ticket.builder()
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNumber)
                .fan(createFanEntity(1))
                .footballMatch(createMatchEntity(
                        1,
                        "2024-03-13T10:30:00-05:00",
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

    private TicketEntity createTicketEntity(int id, Double price, int rowNum, int seatNumber) {
        MatchEntity match = createMatchEntity(
                1,
                "2024-03-13T10:30:00-05:00",
                "Turf Moor",
                "FT",
                "Burnley",
                "https://media.api-sports.io/football/teams/44.png",
                false,
                "Manchester City",
                "https://media.api-sports.io/football/teams/50.png",
                true,
                0,
                3);

        return TicketEntity.builder()
                .id(id)
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNumber)
                .fan(createFanEntity(1))
                .footballMatch(match)
                .build();
    }
    private MatchEntity createMatchEntity(Integer id,
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

        List<TicketEntity> tickets = TicketGenerator.generateTickets(2000);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return MatchEntity.builder()
                .id(id)
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
                .availableTickets(tickets)
                .build();
    }

    private FanEntity createFanEntity(int id) {
        return FanEntity.builder().id(id).boughtTickets(null).build();
    }
}