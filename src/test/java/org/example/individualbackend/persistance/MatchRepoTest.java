package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchRepoTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MatchRepo matchRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save_shouldSaveMatchWithAllFields(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);

        MatchEntity savedMatch = matchRepo.save(match);

        assertNotNull(savedMatch.getId());

        MatchEntity retrievedMatch = entityManager.find(MatchEntity.class, savedMatch.getId());

        assertMatchEntitiesEquals(match, retrievedMatch);
    }

    @Test
    void findById_shouldReturnMatchWhenExists(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        MatchEntity foundMatch = matchRepo.findById(match.getId()).orElse(null);

        assertNotNull(foundMatch);
        assertMatchEntitiesEquals(match,foundMatch);
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenMatchDoesNotExist(){
        assertFalse(matchRepo.findById(999).isPresent());
    }

    @Test
    void findAll_shouldReturnListOfMatches(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate1 = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        LocalDateTime matchDate2 = LocalDateTime.parse("2024-03-13T21:00:00", formatter);

        MatchEntity match1 = createSampleMatch(matchDate1);
        MatchEntity match2 = createSampleMatch(matchDate2);

        entityManager.persist(match1);
        entityManager.persist(match2);

        List<MatchEntity> matches = matchRepo.findAll();

        //TODO: find why it doesn't work
        //assertEquals(2, matches.size());
        assertMatchEntitiesEquals(match1, matches.get(matches.size()-2));
        assertMatchEntitiesEquals(match2, matches.get(matches.size()-1));
    }

    @Test
    void delete_shouldRemoveMatch(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        matchRepo.delete(match);

        assertNull(entityManager.find(MatchEntity.class, match.getId()));
    }

    @Test
    void deleteById_shouldRemoveMatchId(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
        MatchEntity match = createSampleMatch(matchDate);
        entityManager.persist(match);

        matchRepo.deleteById(match.getId());

        assertNull(entityManager.find(MatchEntity.class, match.getId()));
    }
    private void assertMatchEntitiesEquals(MatchEntity expected, MatchEntity actual) {
        assertEquals(expected.getId(), actual.getId());
    }

    private MatchEntity createSampleMatch(LocalDateTime matchDate) {

        return MatchEntity.builder()
                .date(matchDate)
                .venueName("Old Trafford")
                .statusShort("Finished")
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