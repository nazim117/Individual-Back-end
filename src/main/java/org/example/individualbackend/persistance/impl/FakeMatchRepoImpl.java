package org.example.individualbackend.persistance.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class FakeMatchRepoImpl implements MatchRepo {
    private final List<MatchEntity> savedMatches;
    private  static Integer NEXT_ID = 1;
    @Override
    public List<MatchEntity> getAll() {
        return Collections.unmodifiableList(savedMatches);
    }

    @Override
    public MatchEntity findById(Integer id) {
        return savedMatches
                .stream()
                .filter(matchEntity -> Objects.equals(matchEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MatchEntity save(MatchEntity match) {
        match.setId(NEXT_ID);
        NEXT_ID++;
        savedMatches.add(match);
        return match;
    }

    @Override
    public MatchEntity update(MatchEntity match) {
        MatchEntity matchEntity = findById(match.getId());
        matchEntity.setHomeTeamName(match.getHomeTeamName());
        matchEntity.setAwayTeamName(match.getAwayTeamName());
        return matchEntity;
    }

    @Override
    public void delete(Integer id) {
        savedMatches.removeIf(math -> Objects.equals(math.getId(), id));
    }

    @Override
    public int count() {
        return savedMatches.size();
    }
}
