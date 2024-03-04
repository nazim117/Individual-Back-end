package org.example.individualbackend.business.iml;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.GetMatchesUseCase;
import org.example.individualbackend.controller.MatchController;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
import org.example.individualbackend.persistance.MatchRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetMatchesUseCaseImpl implements GetMatchesUseCase {
    private final MatchRepo matchRepo;
    @Override
    public GetAllMatchesResponse getMatches() {
        List<Match> matches = matchRepo.getAll()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetAllMatchesResponse.builder()
                .matches(matches)
                .build();
    }
}
