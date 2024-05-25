package org.example.individualbackend.business.match_service.interfaces;

import org.example.individualbackend.domain.get.GetMatchesResponse;

public interface GetMatchesUseCase {
    GetMatchesResponse getMatchesDescDate();
    GetMatchesResponse getTop6Matches();
    GetMatchesResponse getMatchesAscDate();
    GetMatchesResponse getMatchesBySoldTickets();
}
