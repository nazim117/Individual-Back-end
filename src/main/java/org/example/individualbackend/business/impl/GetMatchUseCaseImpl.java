package org.example.individualbackend.business.impl;

import io.swagger.v3.oas.models.examples.Example;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetMatchUseCase;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetMatchUseCaseImpl implements GetMatchUseCase {
    private final MatchRepo matchRepo;
    @Transactional
    @Override
    public MatchEntity getMatch(Integer id) {

        return matchRepo.getMatchEntityById(id);
    }
}
