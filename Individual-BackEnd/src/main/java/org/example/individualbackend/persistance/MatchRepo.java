package org.example.individualbackend.persistance;

import org.example.individualbackend.domain.Match;

import java.util.List;

public interface MatchRepo {
    public List<Match> getAllMatches();
}
