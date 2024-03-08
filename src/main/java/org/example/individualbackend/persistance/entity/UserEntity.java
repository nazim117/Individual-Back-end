package org.example.individualbackend.persistance.entity;

import lombok.Builder;
import lombok.Data;
import org.example.individualbackend.domain.Ticket;

import java.util.List;
@Builder
@Data
public class UserEntity {
    private Integer id;
    private String email;
    private String fName;
    private String lName;
    private String picture;
    private String password;
    private List<Ticket> tickets;
}
