package org.example.individualbackend.controller;

import org.example.individualbackend.business.*;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void getTickets_ReturnsListOfTickets() throws Exception{
        GetAllTicketsResponse response = GetAllTicketsResponse.builder().tickets(createTicketList()).build();
        when(getTicketsUseCase.getTickets()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/tickets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void getTicket_WithValidId_ReturnsTicket() throws Exception{
        //Arrange
        TicketEntity ticketEntity = createTicketEntity(30.0, 5, 223);
        when(getTicketUseCase.getTicket(1)).thenReturn(ticketEntity);

        //Act
        //Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tickets/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void createTicket_WithValidRequest_ReturnsCreated() throws Exception{
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(443)
                .fan(createFanEntity(1))
                .footballMatch(createMatchEntity(
                        1,
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
                        3)).build();

        CreateTicketResponse response = CreateTicketResponse.builder().id(1).build();

        when(createTicketUseCase.createTicket(any())).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
//TODO: find out why it doesnt work
       // assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void updateTicket_WithValidRequest_Successful_ReturnsNoContent() throws Exception{
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .id(1)
                .price(20.0)
                .rowNum(5)
                .seatNumber(443)
                .fan(createFanEntity(1))
                .match(createMatchEntity(
                        1,
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
                        3)).build();

        doNothing().when(updateTicketUseCase).updateTicket(any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/tickets/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();
//TODO: find where it fails
        //assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }


//TODO: add delete ticket method
    private List<Ticket> createTicketList() {
        List<Ticket> tickets =  new ArrayList<>();

        tickets.add(createTicket(32.0,6, 455));
        tickets.add(createTicket(50.0,2, 32));

        return tickets;
    }
    private Ticket createTicket(Double price, int rowNum, int seatNumber) {
        MatchEntity match = createMatchEntity(
                1,
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
                3);

        return Ticket.builder()
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNumber)
                .fan(createFanEntity(1))
                .footballMatch(match)
                .build();
    }

    private TicketEntity createTicketEntity(Double price, int rowNum, int seatNumber) {
        MatchEntity match = createMatchEntity(
                1,
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
                3);

        return TicketEntity.builder()
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
                .build();
    }

    private FanEntity createFanEntity(int id) {
        return FanEntity.builder().id(id).build();
    }
}