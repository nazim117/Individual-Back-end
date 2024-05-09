package org.example.individualbackend.business.MatchService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchUseCase;
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
