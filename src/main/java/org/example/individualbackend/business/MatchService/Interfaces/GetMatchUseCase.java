package org.example.individualbackend.business.MatchService.Interfaces;

import org.example.individualbackend.persistance.entity.MatchEntity;

public interface GetMatchUseCase {
    MatchEntity getMatch(Integer id);
}
