package org.example.individualbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private static Integer idCounter = 1;
    private Integer id;
    private Double price;
    private Integer rowNum;
    private Integer seatNumber;

}
