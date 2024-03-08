package org.example.individualbackend.persistance.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class MatchEntity {
    private Integer id;
    private String name;
    private String code;
    private Integer founded;
    private String logo;
}
