package org.example.individualbackend.persistance.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class MatchEntity {
    private Integer id;
    private String timezone;
    private LocalDateTime date;
    private String venueName;
    private String statusShort;
    private String homeTeamName;
    private String homeTeamLogo;
    private Boolean homeTeamWinner;
    private String awayTeamName;
    private String awayTeamLogo;
    private Boolean awayTeamWinner;
    private Integer goalsHome;
    private Integer goalsAway;
}
