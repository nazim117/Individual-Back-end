package org.example.individualbackend.business.match_service.interfaces;

import org.example.individualbackend.persistance.entity.MatchEntity;

public interface GetMatchUseCase {
    MatchEntity getMatch(Integer id);
}
