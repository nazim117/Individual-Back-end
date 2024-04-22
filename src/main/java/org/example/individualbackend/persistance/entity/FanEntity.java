package org.example.individualbackend.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "fan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "fan")
    private List<TicketEntity> boughtTickets;
}
