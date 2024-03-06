package org.example.individualbackend.business;

import org.example.individualbackend.persistance.entity.MatchEntity;

public interface GetMatchUseCase {
    MatchEntity getMatch(Integer id);
}
