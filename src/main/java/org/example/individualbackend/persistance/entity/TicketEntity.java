package org.example.individualbackend.persistance.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TicketEntity {
    private Integer id;
    private Double price;
    private Integer rowNum;
    private Integer seatNumber;
}
