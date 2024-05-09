package org.example.individualbackend.controller;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchUseCase;
import org.example.individualbackend.business.MatchService.Interfaces.GetMatchesUseCase;
import org.example.individualbackend.domain.get.GetMatchesResponse;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/matches")
@AllArgsConstructor
public class MatchController {
    private final GetMatchesUseCase getMatchesUseCase;
    private final GetMatchUseCase getMatchUseCase;

    @GetMapping
    public ResponseEntity<GetMatchesResponse> getMatches(){
        return ResponseEntity.ok(getMatchesUseCase.getMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchEntity> getMatch(@PathVariable(value = "id") final Integer id){
        MatchEntity match = getMatchUseCase.getMatch(id);
        if(match == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(match);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<GetMatchesResponse> getUpcomingMatches(){
        return ResponseEntity.ok(getMatchesUseCase.getTop3Matches());
    }
}
