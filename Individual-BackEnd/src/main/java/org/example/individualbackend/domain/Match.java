package org.example.individualbackend.domain;

import java.time.LocalDate;

public class Match {
    private final Integer Id;
    private final String homeTeamName;
    private final String homeTeamImg;
    private final String awayTeamName;
    private final String awayTeamImg;
    private final LocalDate time;
    private final String location;

    public Match(Integer id, String homeTeamName, String homeTeamImg, String awayTeamName, String awayTeamImg, LocalDate time, String location) {
        Id = id;
        this.homeTeamName = homeTeamName;
        this.homeTeamImg = homeTeamImg;
        this.awayTeamName = awayTeamName;
        this.awayTeamImg = awayTeamImg;
        this.time = time;
        this.location = location;
    }
}
