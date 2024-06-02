package org.example.individualbackend.business.match_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.match_service.interfaces.GetMatchUseCase;
import org.example.individualbackend.business.match_service.utilities.MatchConverter;
import org.example.individualbackend.domain.match.Match;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class GetMatchUseCaseImpl implements GetMatchUseCase {
    private final MatchRepo matchRepo;
    @Transactional
    @Override
    public Match getMatch(Integer id) {
        MatchEntity matchEntity= matchRepo.getMatchEntityById(id);
        if(matchEntity==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found");
        }
        return MatchConverter.convert(matchEntity);
    }
}
