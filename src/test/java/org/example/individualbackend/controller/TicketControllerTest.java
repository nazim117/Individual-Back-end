package org.example.individualbackend.controller;

import org.example.individualbackend.business.*;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

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

    //TODO: FIX GETTICKETS METHOD
//    @Test
//    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
//     void getTickets_ReturnsListOfTickets() throws Exception{
//        GetAllTicketsResponse response = GetAllTicketsResponse.builder().tickets(createTicketList()).build();
//        when(getTicketsUseCase.getTickets()).thenReturn(response);
//
//        mockMvc.perform(get("/tickets"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().json("""
//                            {
//                                  "tickets": [
//                                      {
//                                          "price": 20.0,
//                                          "rowNum": 5,
//                                          "seatNumber": 443,
//                                          "fanId": 1,
//                                          "footballMatchId": 1
//                                      },
//                                      {
//                                          "price": 20.0,
//                                          "rowNum": 5,
//                                          "seatNumber": 443,
//                                          "fanId": 1,
//                                          "footballMatchId": 1
//                                      }
//                                  ]
//                              }
//                        """));
//    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void getTicket_WithValidId_ReturnsTicket() throws Exception{
        Mockito.when(getTicketUseCase.getTicket(Mockito.anyInt())).thenReturn(createTicketEntity(1, 20.0, 5, 70));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/tickets/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void createTicket_WithValidRequest_ReturnsCreated() throws Exception{
        CreateTicketRequest request = CreateTicketRequest.builder()
                .price(20.0)
                .rowNum(5)
                .seatNumber(443)
                .fanId(1)
                .footballMatchId(1).build();

        CreateTicketResponse response = CreateTicketResponse.builder().id(1).build();

        when(createTicketUseCase.createTicket(any())).thenReturn(response);

        mockMvc.perform(post("/tickets")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "price": 20.0,
                                  "rowNum": 5,
                                  "seatNumber": 443,
                                  "fanId": 1,
                                  "footballMatchId": 1
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            { "id": 1 }
                        """));

        verify(createTicketUseCase).createTicket(request);

    }

    @Test
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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/tickets/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void deleteTicket_ValidId_ReturnsNoContent() throws Exception {
        Integer ticketId = 1;

        doNothing().when(deleteTicketUseCase).deleteTicket(ticketId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/tickets/{id}", ticketId))
                .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
    private List<Ticket> createTicketList() {
        List<Ticket> tickets =  new ArrayList<>();

        tickets.add(createTicket(32.0,6, 455));
        tickets.add(createTicket(50.0,2, 32));

        return tickets;
    }
    private Ticket createTicket(Double price, int rowNum, int seatNumber) {
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
                .availableTickets(0)
                .build();
    }

    private FanEntity createFanEntity(int id) {
        return FanEntity.builder().id(id).boughtTickets(null).build();
    }
}