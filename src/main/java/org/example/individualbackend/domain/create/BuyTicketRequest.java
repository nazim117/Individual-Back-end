package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BuyTicketRequest {
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
    private int footballMatchId;
}
