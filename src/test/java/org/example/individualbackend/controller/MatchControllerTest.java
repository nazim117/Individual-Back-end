package org.example.individualbackend.controller;

import org.example.individualbackend.business.GetMatchUseCase;
import org.example.individualbackend.business.GetMatchesUseCase;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(MatchController.class)
public class MatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GetMatchesUseCase getMatchesUseCase;

    @Mock
    private GetMatchUseCase getMatchUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getMatch_ReturnsMatchEntityList() throws Exception {
        //Arrange
        GetAllMatchesResponse response = GetAllMatchesResponse
                .builder()
                .matches(List.of(
                        Match.builder()
                                .id(1)
                                .date(LocalDateTime.parse("2023-08-11T19:00:00"))
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
                                .date(LocalDateTime.parse("2023-08-12T11:30:00"))
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
                                        "date": "2023-08-11T19:00:00",
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
                                    "date": "2023-08-12T11:30:00",
                                    "venueName": "Emirates Stadium",
                                    "statusShort":"FT",
                                    "homeTeamName": "Arsenal",
                                    "homeTeamLogo": "https://media.api-sports.io/football/teams/42.png",
                                    "homeTeamWinner": false,
                                    "awayTeamName":"Nottingham Forest",
                                    "awayTeamLogo": "https://media.api-sports.io/football/teams/65.png",
                                    "awayTeamWinner": true,
                                    "goalsHome": 0,
                                    "goalsAway":3
                                  }
                            ]}
                        """));
    }

    @Test
    public void getMatch_ReturnsMatchEntity() throws Exception{
        MatchEntity mockMatchEntity = MatchEntity.builder()
                .id(1)
                .date(LocalDateTime.parse("2023-08-11T19:00:00"))
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

        mockMvc.perform(get("/matches/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockMatchEntity.getId()));
    }

    @Test
    public void getMatch_ReturnsNotFoundForNONExistentMatch() throws Exception{
        when(getMatchUseCase.getMatch(anyInt())).thenReturn(null);

        mockMvc.perform(get("/matches/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}