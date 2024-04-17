package org.example.individualbackend.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.domain.Ticket;
import org.hibernate.validator.constraints.Length;

import java.util.List;
@Entity
@Table(name="user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Length(min = 5, max = 50)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "fName")
    private String fName;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "lName")
    private String lName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "picture")
    private String picture;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<TicketEntity> boughtTickets;
}
