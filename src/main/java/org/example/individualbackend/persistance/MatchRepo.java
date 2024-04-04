package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.MatchEntity;

import java.util.List;

public interface MatchRepo {
    List<MatchEntity> getAllMatches(String leagueId, String seasonId);
    MatchEntity findById(Integer id);
    boolean save();
    int count();
}
