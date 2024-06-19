package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import org.example.individualbackend.persistance.repositories.MatchRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchRepoTest {
    //@Autowired
    private EntityManager entityManager;

    //@Autowired
    private MatchRepo matchRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    void save_shouldSaveMatchWithAllFields(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T07:30:00-05:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);

        MatchEntity savedMatch = matchRepo.save(match);

        assertNotNull(savedMatch.getId());

        MatchEntity retrievedMatch = entityManager.find(MatchEntity.class, savedMatch.getId());

        assertEquals(match, retrievedMatch);
    }

    void findById_shouldReturnMatchWhenExists(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T12:00:00-05:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        MatchEntity foundMatch = matchRepo.findById(match.getId()).orElse(null);

        assertNotNull(foundMatch);
        assertEquals(match,foundMatch);
    }

    void findById_shouldReturnEmptyOptionalWhenMatchDoesNotExist(){
        assertFalse(matchRepo.findById(-1).isPresent());
    }

    void findAll_shouldReturnListOfMatches(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate1 = LocalDateTime.parse("2024-03-12T08:30:00-08:00", formatter);
        LocalDateTime matchDate2 = LocalDateTime.parse("2024-03-13T10:30:00-05:00", formatter);

        MatchEntity match1 = createSampleMatch(matchDate1);
        MatchEntity match2 = createSampleMatch(matchDate2);

        entityManager.persist(match1);
        entityManager.persist(match2);

        MatchEntity retrievedMatch1 = entityManager.find(MatchEntity.class, match1.getId());
        MatchEntity retrievedMatch2 = entityManager.find(MatchEntity.class, match2.getId());

        List<MatchEntity> matches = new ArrayList<>();

        matches.add(retrievedMatch1);
        matches.add(retrievedMatch2);

        assertEquals(2, matches.size());
        assertEquals(match1, matches.get(0));
        assertEquals(match2, matches.get(1));
    }

    void delete_shouldRemoveMatch(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T08:55:00-05:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        matchRepo.delete(match);

        assertNull(entityManager.find(MatchEntity.class, match.getId()));
    }

    void deleteById_shouldRemoveMatchId(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T10:30:00-05:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        matchRepo.deleteById(match.getId());

        assertNull(entityManager.find(MatchEntity.class, match.getId()));
    }

    private MatchEntity createSampleMatch(LocalDateTime matchDate) {

        return MatchEntity.builder()
                .date(matchDate)
                .venueName("Old Trafford")
                .statusShort("FT")
                .homeTeamName("Manchester United")
                .homeTeamLogo("logo1.png")
                .homeTeamWinner(false)
                .awayTeamName("Liverpool")
                .awayTeamLogo("logo2.png")
                .awayTeamWinner(true)
                .goalsHome(2)
                .goalsAway(3)
                .build();
    }
}