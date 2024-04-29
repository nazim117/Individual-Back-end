package org.example.individualbackend.persistance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "football_match")
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
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime date;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "venueName")
    private String venueName;

    @NotBlank
    @Length(min = 2, max = 10)
    @Column(name = "statusShort")
    private String statusShort;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "homeTeamName")
    private String homeTeamName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "homeTeamLogo")
    private String homeTeamLogo;

    @NotNull
    private Boolean homeTeamWinner;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "awayTeamName")
    private String awayTeamName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "awayTeamLogo")
    private String awayTeamLogo;

    @NotNull
    private Boolean awayTeamWinner;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "goalsHome")
    private Integer goalsHome;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "goalsAway")
    private Integer goalsAway;

    private int availableTickets;
}
