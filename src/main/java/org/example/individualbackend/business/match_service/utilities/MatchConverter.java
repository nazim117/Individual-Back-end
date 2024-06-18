package org.example.individualbackend.business.match_service.utilities;

import org.example.individualbackend.business.ticket_service.utilities.TicketConverter;
import org.example.individualbackend.domain.match.Match;
import org.example.individualbackend.domain.ticket.Ticket;
import org.example.individualbackend.persistance.entity.MatchEntity;

import java.util.Collections;
import java.util.List;

public class MatchConverter {

    private MatchConverter(){

    }
    public static Match convert(MatchEntity match){
        List<Ticket> convertedTickets = (match.getAvailableTickets() != null)
                ? match.getAvailableTickets().stream()
                .map(TicketConverter::convert)
                .toList()
                : Collections.emptyList();
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
                .availableTickets(convertedTickets)
                .availableTicketsCount(match.getAvailableTicketCount())
                .soldTicketCount(match.getSoldTicketCount())
                .build();
    }
}
