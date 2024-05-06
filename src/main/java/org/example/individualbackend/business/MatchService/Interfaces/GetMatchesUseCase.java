package org.example.individualbackend.business.MatchService.Interfaces;

import org.example.individualbackend.domain.get.GetMatchesResponse;

public interface GetMatchesUseCase {
    GetMatchesResponse getMatches();
    GetMatchesResponse getTop3Matches();
}
