package org.example.individualbackend.domain.get;

import jakarta.validation.constraints.NotBlank;

public class GetAllMatchesRequest {
    @NotBlank
    private String leagueId;
    @NotBlank
    private String seasonId;

}
