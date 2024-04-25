package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private double price;

    @NotNull
    private Integer rowNum;

    @NotNull
    private Integer seatNumber;

    @NotNull
    private FanEntity fan;

    @NotNull
    private MatchEntity footballMatch;
}
