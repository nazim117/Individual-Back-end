package org.example.individualbackend.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="ticket")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Min(0)
    @Max(500)
    @Column(name = "price")
    private Double price;

    @NotNull
    @Min(1)
    @Max(20)
    @Column(name = "rowNum")
    private Integer rowNum;

    @NotNull
    @Min(1)
    @Max(5000)
    @Column(name = "seatNumber")
    private Integer seatNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "football_match_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MatchEntity footballMatch;
}
