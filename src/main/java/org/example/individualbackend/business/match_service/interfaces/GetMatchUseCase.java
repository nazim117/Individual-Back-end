package org.example.individualbackend.business.match_service.interfaces;

import org.example.individualbackend.domain.match.Match;

public interface GetMatchUseCase {
    Match getMatch(Integer id);
}
