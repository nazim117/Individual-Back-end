package org.example.individualbackend.business.match_service.interfaces;

import org.example.individualbackend.domain.get.GetMatchesResponse;

public interface GetMatchesUseCase {
    GetMatchesResponse getMatches();
    GetMatchesResponse getTop3Matches();
}
