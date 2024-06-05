package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    @NotNull
    @Min(0)
    @Max(1000)
    private double price;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer rowNum;
    @NotNull
    @Min(1)
    @Max(1000)
    private Integer seatNumber;
    @NotNull
    private int fanId;
    @NotNull
    private int footballMatchId;
}
