package org.example.individualbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.*;
import org.example.individualbackend.domain.get.GetAllMatchesResponse;
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
    @RolesAllowed({"FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<GetAllMatchesResponse> getMatches(){
        return ResponseEntity.ok(getMatchesUseCase.getMatches());
    }

    @GetMapping("{id}")
    @RolesAllowed({"FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<MatchEntity> getMatch(@PathVariable(value = "id") final Integer id){
        MatchEntity match = getMatchUseCase.getMatch(id);
        if(match == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(match);
    }
}
