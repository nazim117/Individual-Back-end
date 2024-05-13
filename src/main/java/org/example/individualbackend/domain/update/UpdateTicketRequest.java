package org.example.individualbackend.domain.update;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketRequest {
    private Integer id;
    @NotNull
    private Double price;
    @NotNull
    private Integer rowNum;
    @NotNull
    private Integer seatNumber;
    @NotNull
    private int fanId;
    @NotNull
    private int matchId;
}
