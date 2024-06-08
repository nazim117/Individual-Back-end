package org.example.individualbackend.business.match_service.utilities;

import org.example.individualbackend.business.ticket_service.utilities.TicketConverter;
import org.example.individualbackend.domain.match.Match;
import org.example.individualbackend.persistance.entity.MatchEntity;

import java.util.Collections;

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
                .availableTickets(match.getAvailableTickets().stream()
                        .map(TicketConverter::convert)
                        .toList())
                .availableTicketsCount(match.getAvailableTicketCount())
                .soldTicketCount(match.getSoldTicketCount())
                .build();
    }
}
