package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    @NotBlank
    private Double price;

    @NotBlank
    private Integer rowNum;

    @NotBlank
    private Integer seatNumber;

    @NotBlank
    private FanEntity fan;

    @NotBlank
    private MatchEntity footballMatch;
}
