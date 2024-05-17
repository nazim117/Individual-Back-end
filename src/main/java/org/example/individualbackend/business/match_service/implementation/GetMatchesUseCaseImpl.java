package org.example.individualbackend.business.match_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.match_service.interfaces.GetMatchesUseCase;
import org.example.individualbackend.business.match_service.utilities.MatchConverter;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetMatchesUseCaseImpl implements GetMatchesUseCase {
    private final SaveMatches saveMatches;
    @Transactional
    @Override
    public GetMatchesResponse getMatches() {
        List<Match> matches = saveMatches.getMatchesData()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder()
                .matches(matches)
                .build();
    }

    @Transactional
    @Override
    public GetMatchesResponse getTop3Matches() {
        List<Match> matches = saveMatches.getTop3MatchesData()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder().matches(matches).build();
    }
}
