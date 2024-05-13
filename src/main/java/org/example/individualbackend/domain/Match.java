package org.example.individualbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    private Integer id;
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
    private List<Ticket> availableTickets;

}
