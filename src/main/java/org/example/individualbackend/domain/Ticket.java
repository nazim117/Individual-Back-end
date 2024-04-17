package org.example.individualbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.domain.users.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Integer id;
    private Double price;
    private Integer rowNum;
    private Integer seatNumber;
    private User user;
    private Match match;
}
