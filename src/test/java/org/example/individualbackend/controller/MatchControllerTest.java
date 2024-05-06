package org.example.individualbackend.controller;

import org.example.individualbackend.business.MatchService.Interfaces.GetMatchUseCase;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchesUseCase;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetMatchesUseCase getMatchesUseCase;

    @MockBean
    private GetMatchUseCase getMatchUseCase;

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void getMatch_ReturnsMatchEntityList() throws Exception {
        //Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        GetMatchesResponse response = GetMatchesResponse
                .builder()
                .matches(List.of(
                        Match.builder()
                                .id(1)
                                .date(LocalDateTime.parse("2023-08-11T09:30:00-05:00", formatter))
                                .venueName("Turf Moor")
                                .statusShort("FT")
                                .homeTeamName("Burnley")
                                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                                .homeTeamWinner(false)
                                .awayTeamName("Manchester City")
                                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                                .awayTeamWinner(true)
                                .goalsHome(0)
                                .goalsAway(3)
                                .build(),
                        Match.builder()
                                .id(2)
                                .date(LocalDateTime.parse("2023-08-11T09:30:00-05:00",formatter))
                                .venueName("Emirates Stadium")
                                .statusShort("FT")
                                .homeTeamName("Arsenal")
                                .homeTeamLogo("https://media.api-sports.io/football/teams/42.png")
                                .homeTeamWinner(true)
                                .awayTeamName("Nottingham Forest")
                                .awayTeamLogo("https://media.api-sports.io/football/teams/65.png")
                                .awayTeamWinner(false)
                                .goalsHome(2)
                                .goalsAway(1)
                                .build()
                ))
                .build();
        when(getMatchesUseCase.getMatches())
                .thenReturn(response);

        //Act
        //Assert
        mockMvc.perform(get("/matches"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {
                                "matches":[
                                {
                                    "id": 1,
                                        "date": "2023-08-11T09:30:00",
                                        "venueName": "Turf Moor",
                                        "statusShort":"FT",
                                        "homeTeamName": "Burnley",
                                        "homeTeamLogo": "https://media.api-sports.io/football/teams/44.png",
                                        "homeTeamWinner": false,
                                        "awayTeamName":"Manchester City",
                                        "awayTeamLogo": "https://media.api-sports.io/football/teams/50.png",
                                        "awayTeamWinner": true,
                                        "goalsHome": 0,
                                        "goalsAway":3
                                },
                                  {
                                    "id": 2,
                                    "date": "2023-08-11T09:30:00",
                                    "venueName": "Emirates Stadium",
                                    "statusShort":"FT",
                                    "homeTeamName": "Arsenal",
                                    "homeTeamLogo": "https://media.api-sports.io/football/teams/42.png",
                                    "homeTeamWinner": true,
                                    "awayTeamName":"Nottingham Forest",
                                    "awayTeamLogo": "https://media.api-sports.io/football/teams/65.png",
                                    "awayTeamWinner": false,
                                    "goalsHome": 2,
                                    "goalsAway":1
                                  }
                            ]}
                        """));
    }
    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void getMatch_ReturnsMatchEntity() throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        MatchEntity mockMatchEntity = MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-11T09:30:00-05:00", formatter))
                .venueName("Turf Moor")
                .statusShort("FT")
                .homeTeamName("Burnley")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(0)
                .goalsAway(3)
                .build();

        when(getMatchUseCase.getMatch(anyInt())).thenReturn(mockMatchEntity);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/matches/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    public void getMatch_ReturnsNotFoundForNONExistentMatch() throws Exception{
        when(getMatchUseCase.getMatch(anyInt())).thenReturn(null);

        mockMvc.perform(get("/matches/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}