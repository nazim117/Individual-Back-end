package org.example.individualbackend.business.impl;

import org.example.individualbackend.domain.Match;
import org.example.individualbackend.persistance.entity.MatchEntity;

public class MatchConverter {
    public static Match convert(MatchEntity match){
        return Match.builder().id(match.getId())
                .id(match.getId())
                .name(match.getName())
                .code(match.getCode())
                .logo(match.getLogo())
                .founded(match.getFounded())
                .build();
    }
}
