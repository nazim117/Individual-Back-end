package org.example.individualbackend.persistance.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Primary
@AllArgsConstructor
public class FakeMatchRepoImpl implements MatchRepo {
    private final List<MatchEntity> savedMatches;
    private final ReadAPIKey readAPIKey = new ReadAPIKey();
    private  final String API_KEY = readAPIKey.readApiKeyFromFile();
    private static final String FOOTBALL_API_TEAMS_URL = "https://media.api-sports.io/football/teams?league={leagueId}&season={seasonId}";
    private  static Integer NEXT_ID = 1;
    @Override
    public List<MatchEntity> getAllMatches(String leagueId, String seasonId) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try{
            Map<String, String> params = Map.of("leagueId", leagueId, "seasonId", seasonId);
        }catch (HttpClientErrorException e){
            return null;
        }
        return Collections.unmodifiableList(savedMatches);
    }

    @Override
    public MatchEntity findById(Integer id) {
        return savedMatches
                .stream()
                .filter(matchEntity -> Objects.equals(matchEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MatchEntity save(MatchEntity match) {
        match.setId(NEXT_ID);
        NEXT_ID++;
        savedMatches.add(match);
        return match;
    }

    @Override
    public int count() {
        return savedMatches.size();
    }
}
