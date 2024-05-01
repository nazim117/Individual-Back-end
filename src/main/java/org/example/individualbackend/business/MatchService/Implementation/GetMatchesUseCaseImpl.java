package org.example.individualbackend.business.MatchService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchesUseCase;
import org.example.individualbackend.business.MatchService.Utilities.MatchConverter;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetMatchesUseCaseImpl implements GetMatchesUseCase {
    private final SaveMatches saveMatches;
    @Transactional
    @Override
    public GetAllMatchesResponse getMatches() {
        List<Match> matches = saveMatches.getMatchesData()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetAllMatchesResponse.builder()
                .matches(matches)
                .build();
    }
}
