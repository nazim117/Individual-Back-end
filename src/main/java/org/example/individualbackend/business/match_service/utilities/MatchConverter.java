package org.example.individualbackend.business.match_service.utilities;

import org.example.individualbackend.domain.Match;
import org.example.individualbackend.persistance.entity.MatchEntity;

public class MatchConverter {

    private MatchConverter(){

    }
    public static Match convert(MatchEntity match){
        return Match.builder()
                .id(match.getId())
                .date(match.getDate())
                .venueName(match.getVenueName())
                .statusShort(match.getStatusShort())
                .homeTeamName(match.getHomeTeamName())
                .homeTeamLogo(match.getHomeTeamLogo())
                .homeTeamWinner(match.getHomeTeamWinner())
                .awayTeamName(match.getAwayTeamName())
                .awayTeamLogo(match.getAwayTeamLogo())
                .awayTeamWinner(match.getAwayTeamWinner())
                .goalsHome(match.getGoalsHome())
                .goalsAway(match.getGoalsAway())
                .build();
    }
}
