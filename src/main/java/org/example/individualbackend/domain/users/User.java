package org.example.individualbackend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.domain.ticket.Ticket;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String fName;
    private String lName;
    private String password;
    private List<Ticket> boughtTickets;

}
