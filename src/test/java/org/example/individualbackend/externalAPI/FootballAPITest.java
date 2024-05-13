package org.example.individualbackend.externalAPI;

import org.example.individualbackend.Utils.TicketGenerator;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FootballAPITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FootballAPI footballAPI;

//    @Test
//     void getMatches_RetrievesDataSuccessfully() throws Exception {
//        // Arrange
//        List<MatchEntity> matches = createMockMatchEntries();
//
//        // Act
//        when(footballAPI.fetchMatchesData()).thenReturn(matches);
//
//        // Assert
//        mockMvc.perform(get("https://v3.football.api-sports.io/fixtures?league=39&season=2023"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().json("""
//                            {
//                                  "matches": [
//                                      {
//                                          "id": 1,
//                                          "date": "2024-03-12T09:30:00",
//                                          "venueName": "Old Trafford",
//                                          "statusShort": "FT",
//                                          "homeTeamName": "Manchester United",
//                                          "homeTeamLogo": "logo1.png",
//                                          "homeTeamWinner": false,
//                                          "awayTeamName": "Liverpool",
//                                          "awayTeamLogo": "logo2.png",
//                                          "awayTeamWinner": true,
//                                          "goalsHome": 2,
//                                          "goalsAway": 3,
//                                          "availableTickets": 5
//                                      },
//                                      {
//                                          "id": 2,
//                                          "date": "2024-03-13T10:30:00",
//                                          "venueName": "Anfield",
//                                          "statusShort": "FT",
//                                          "homeTeamName": "Liverpool",
//                                          "homeTeamLogo": "lvlogo.png",
//                                          "homeTeamWinner": true,
//                                          "awayTeamName": "Arsenal",
//                                          "awayTeamLogo": "arslogo.png",
//                                          "awayTeamWinner": false,
//                                          "goalsHome": 4,
//                                          "goalsAway": 1,
//                                          "availableTickets": 5
//                                      }
//                                  ]
//                              }
//                        """));
//    }
//        @Test
//     void getMatches_FailsToRetrieveData(){
//        //Arrange
//        //Act
//        when(footballAPI.fetchMatchesData()).thenReturn(null);
//
//        //Assert
//    }

    private List<MatchEntity> createMockMatchEntries() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate1 = LocalDateTime.parse("2024-03-12T09:30:00-05:00", formatter);
        LocalDateTime matchDate2 = LocalDateTime.parse("2024-03-13T10:30:00-05:00", formatter);

        List<TicketEntity> ticketEntities = TicketGenerator.getInstance().generateTicket(2,5);

        List<MatchEntity> mockMatches = new ArrayList<>();
        mockMatches.add(MatchEntity.builder()
                        .id(1)
                        .date(matchDate1)
                        .venueName("Old Trafford")
                        .statusShort("FT")
                        .homeTeamName("Manchester United")
                        .homeTeamLogo("logo1.png")
                        .homeTeamWinner(false)
                        .awayTeamName("Liverpool")
                        .awayTeamLogo("logo2.png")
                        .awayTeamWinner(true)
                        .goalsHome(2)
                        .goalsAway(3)
                        .availableTickets(ticketEntities)
                        .build());

        mockMatches.add(MatchEntity.builder()
                .id(2)
                .date(matchDate2)
                .venueName("Anfield")
                .statusShort("FT")
                .homeTeamName("Liverpool")
                .homeTeamLogo("lvlogo.png")
                .homeTeamWinner(true)
                .awayTeamName("Arsenal")
                .awayTeamLogo("arslogo.png")
                .awayTeamWinner(false)
                .goalsHome(4)
                .goalsAway(1)
                .availableTickets(ticketEntities)
                .build());

        return mockMatches;
    }
}