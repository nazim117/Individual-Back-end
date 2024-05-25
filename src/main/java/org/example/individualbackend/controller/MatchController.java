package org.example.individualbackend.controller;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.match_service.interfaces.GetMatchUseCase;
import org.example.individualbackend.business.match_service.interfaces.GetMatchesUseCase;
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

    @GetMapping("/descending")
    public ResponseEntity<GetMatchesResponse> getMatchesDescDate(){
        return ResponseEntity.ok(getMatchesUseCase.getMatchesDescDate());
    }
    @GetMapping("/ascending")
    public ResponseEntity<GetMatchesResponse> getMatchesAscDate(){
        return ResponseEntity.ok(getMatchesUseCase.getMatchesAscDate());
    }

    @GetMapping("/most-sold")
    public ResponseEntity<GetMatchesResponse> getMatchesByMostSoldTickets(){
        return ResponseEntity.ok(getMatchesUseCase.getMatchesBySoldTickets());
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
        return ResponseEntity.ok(getMatchesUseCase.getTop6Matches());
    }
}
