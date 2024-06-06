package org.example.individualbackend.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="tickets")
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
    private double price;

    @NotNull
    @Min(1)
    @Max(20)
    @Column(name = "row_num")
    private Integer rowNum;

    @NotNull
    @Min(1)
    @Max(5000)
    @Column(name = "seat_num")
    private Integer seatNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="fan_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FanEntity fan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MatchEntity footballMatch;
}
