package org.example.individualbackend.persistance;

import org.example.individualbackend.persistance.entity.MatchEntity;

import java.util.List;

public interface MatchRepo {
    List<MatchEntity> getAll();
    MatchEntity findById(Integer id);
    public MatchEntity save(MatchEntity match);
    MatchEntity update(MatchEntity match);
    void delete(Integer id);

    int count();
}
