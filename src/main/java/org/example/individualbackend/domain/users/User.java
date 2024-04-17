package org.example.individualbackend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.domain.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String fName;
    private String lName;
    private String picture;
    private String password;
    private List<Ticket> boughtTickets;

}
