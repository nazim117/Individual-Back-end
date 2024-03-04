package org.example.individualbackend.business.iml;

import org.example.individualbackend.domain.Match;
import org.example.individualbackend.persistance.entity.MatchEntity;

public class MatchConverter {
    public static Match convert(MatchEntity match){
        return Match.builder().id(match.getId())
                .homeTeamName(match.getHomeTeamName())
                .awayTeamName(match.getAwayTeamName())
                .location(match.getLocation())
                .time(match.getTime())
                .build();
    }
}
