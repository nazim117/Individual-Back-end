package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetMatchesUseCase;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
import org.example.individualbackend.persistance.MatchRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetMatchesUseCaseImpl implements GetMatchesUseCase {
    private final MatchRepo matchRepo;
    @Transactional
    @Override
    public GetAllMatchesResponse getMatches() {
        List<Match> matches = matchRepo.getMatchEntitiesBy()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetAllMatchesResponse.builder()
                .matches(matches)
                .build();
    }
}
