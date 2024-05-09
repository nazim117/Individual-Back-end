package org.example.individualbackend.business.MatchService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.Utils.TicketGenerator;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchesUseCase;
import org.example.individualbackend.business.MatchService.Utilities.MatchConverter;
import org.example.individualbackend.domain.Match;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.hibernate.engine.jdbc.mutation.TableInclusionChecker;
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
