package org.example.individualbackend.domain;

import lombok.*;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Integer id;
    private Double price;
    private Integer rowNum;
    private Integer seatNumber;
    private FanEntity fan;
    private MatchEntity footballMatch;
}
