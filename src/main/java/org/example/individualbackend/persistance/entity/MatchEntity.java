package org.example.individualbackend.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "date")
    private LocalDateTime date;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "venue_name")
    private String venueName;

    @NotNull
    @Min(0)
    @Max(200_000)
    @Column(name = "venue_capacity")
    private int venueCapacity;

    @NotBlank
    @Length(min = 2, max = 10)
    @Column(name = "status_short")
    private String statusShort;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "home_team_name")
    private String homeTeamName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "home_team_logo")
    private String homeTeamLogo;

    @NotNull
    @Column(name = "home_team_winner")
    private Boolean homeTeamWinner;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "away_team_name")
    private String awayTeamName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "away_team_logo")
    private String awayTeamLogo;

    @NotNull
    @Column(name = "away_team_winner")
    private Boolean awayTeamWinner;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "goals_home")
    private Integer goalsHome;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "goals_away")
    private Integer goalsAway;

    @OneToMany(mappedBy = "footballMatch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<TicketEntity> availableTickets;

    @JsonIgnore
    public int getAvailableTicketCount(){
        return availableTickets != null ? (int) availableTickets.stream().filter(ticket -> ticket.getFan() == null).count() : 0;
    }

    @JsonIgnore
    public int getSoldTicketCount(){
        return availableTickets != null ? (int) availableTickets.stream().filter(ticket -> ticket.getFan() != null).count() : 0;
    }
}
