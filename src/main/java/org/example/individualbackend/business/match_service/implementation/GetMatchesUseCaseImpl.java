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
    public GetMatchesResponse getMatchesDescDate() {
        List<Match> matches = saveMatches.getMatchesDataDescDate()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder()
                .matches(matches)
                .build();
    }

    @Transactional
    @Override
    public GetMatchesResponse getTop6Matches() {
        List<Match> matches = saveMatches.getTop6MatchesData()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder().matches(matches).build();
    }

    @Transactional
    @Override
    public GetMatchesResponse getMatchesAscDate() {
        List<Match> matches = saveMatches.getMatchesAscDate()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder().matches(matches).build();
    }

    @Override
    public GetMatchesResponse getMatchesBySoldTickets() {
        List<Match> matches = saveMatches.getMatchesByMostSoldTickets()
                .stream()
                .map(MatchConverter::convert)
                .toList();

        return GetMatchesResponse.builder().matches(matches).build();
    }
}
