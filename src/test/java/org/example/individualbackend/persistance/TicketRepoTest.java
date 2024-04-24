package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepoTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TicketRepo ticketRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_ShouldSaveTicketWithAllFields(){
        TicketEntity ticketEntity = createTicketEntity(20.0, 5,721);

        entityManager.persist(ticketEntity);
        entityManager.flush();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        assertNotNull(ticketEntity.getId());
        assertEquals(20.0, ticketEntity.getPrice());
        assertEquals(5, ticketEntity.getRowNum());
        assertEquals(721, ticketEntity.getSeatNumber());
        assertEquals(FanEntity.builder().id(1).build(), ticketEntity.getFan());
        assertEquals(MatchEntity.builder()
                .id(ticketEntity.getFootballMatch().getId())
                .date(LocalDateTime.parse("2023-08-11T19:00:00", formatter))
                .venueName("Turf Moor")
                .statusShort("FT")
                .homeTeamName("Burnley")
                .homeTeamLogo("https://media.api-sports.io/football/teams/44.png")
                .homeTeamWinner(false)
                .awayTeamName("Manchester City")
                .awayTeamLogo("https://media.api-sports.io/football/teams/50.png")
                .awayTeamWinner(true)
                .goalsHome(0)
                .goalsAway(3)
                .build(), ticketEntity.getFootballMatch());
    }

    @Test
    public void saveTicket_InvalidInput_ShouldThrowException(){
        MatchEntity match = createMatchEntity(
                1,
                "2023-08-11T19:00:00",
                "Turf Moor",
                "FT",
                "Burnley",
                "https://media.api-sports.io/football/teams/44.png",
                false,
                "Manchester City",
                "https://media.api-sports.io/football/teams/50.png",
                true,
                0,
                3);

        match = entityManager.merge(match);

        TicketEntity ticketEntity = TicketEntity.builder()
                .price(-1.0)
                .rowNum(5)
                .seatNumber(290)
                .fan(createFanEntity(1))
                .footballMatch(match)
                .build();

        assertThrows(ConstraintViolationException.class, () -> ticketRepo.save(ticketEntity));

    }

    @Test
    public void findById_ShouldReturnTicketEntity(){
        //Arrange
        TicketEntity ticketEntity = createTicketEntity(20.0, 5,721);
        ticketRepo.save(ticketEntity);

        //Act
        TicketEntity foundTicketEntity = ticketRepo.findById(ticketEntity.getId()).orElse(null);

        //Assert
        assertNotNull(foundTicketEntity);
        assertEquals(ticketEntity.getId(), foundTicketEntity.getId());
    }

    @Test
    public void findById_InvalidId_ShouldReturnNull(){
        //Arrange
        //Act
        TicketEntity ticketEntity = ticketRepo.findById(999).orElse(null);

        //Assert
        assertNull(ticketEntity);
    }

    @Test
    public void existsByRowNumAndSeatNumber_ValidTicket_ReturnsTrue(){
        TicketEntity ticketEntity = createTicketEntity(20.0, 5,721);

        ticketRepo.save(ticketEntity);

        boolean exists = ticketRepo.existsByRowNumAndSeatNumber(ticketEntity.getRowNum(), ticketEntity.getSeatNumber());

        assertTrue(exists);
    }

    @Test
    public void existsByRowNumAndSeatNumber_InvalidTicket_ReturnsFalse(){
        //Arrange
        //Act
        boolean exists = ticketRepo.existsByRowNumAndSeatNumber(4, 322);

        //Assert
        assertFalse(exists);
    }

    @Test
    public void deleteById_ValidId_ReturnsTrue(){
        //Arrange
        TicketEntity ticketEntity = createTicketEntity(20.0, 5,721);
        ticketRepo.save(ticketEntity);

        //Act
        ticketRepo.deleteById(ticketEntity.getId());

        //Assert
        assertFalse(ticketRepo.existsById(ticketEntity.getId()));
    }

    @Test
    public void findAllTickets_ReturnsAllTickets(){
        TicketEntity ticketEntity1 = createTicketEntity(20.0, 5,721);
        TicketEntity ticketEntity2 = createTicketEntity(30.0, 2,112);
        TicketEntity ticketEntity3 = createTicketEntity(65.9, 1,20);

        ticketRepo.saveAll(List.of(ticketEntity1, ticketEntity2, ticketEntity3));

        List<TicketEntity> allTickets = ticketRepo.findAll();

        assertEquals(3, allTickets.size());
    }
    private TicketEntity createTicketEntity(Double price, Integer rowNum, Integer seatNum) {
        MatchEntity match = createMatchEntity(
                1,
                "2023-08-11T19:00:00",
                "Turf Moor",
                "FT",
                "Burnley",
                "https://media.api-sports.io/football/teams/44.png",
                false,
                "Manchester City",
                "https://media.api-sports.io/football/teams/50.png",
                true,
                0,
                3);

        match = entityManager.merge(match);

        return TicketEntity.builder()
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNum)
                .fan(createFanEntity(1))
                .footballMatch(match)
                .build();
    }

    private MatchEntity createMatchEntity(Integer id,
                                          String _date,
                                          String _venueName,
                                          String _statusShort,
                                          String  _homeTeamName,
                                          String _homeTeamLogo,
                                          Boolean  _homeTeamWinner,
                                          String _awayTeamName,
                                          String _awayTeamLogo,
                                          Boolean _awayTeamWinner,
                                          Integer _goalsHome,
                                          Integer _goalsAway) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return MatchEntity.builder()
                .id(id)
                .date(LocalDateTime.parse(_date, formatter))
                .venueName(_venueName)
                .statusShort(_statusShort)
                .homeTeamName(_homeTeamName)
                .homeTeamLogo(_homeTeamLogo)
                .homeTeamWinner(_homeTeamWinner)
                .awayTeamName(_awayTeamName)
                .awayTeamLogo(_awayTeamLogo)
                .awayTeamWinner(_awayTeamWinner)
                .goalsHome(_goalsHome)
                .goalsAway(_goalsAway)
                .build();
    }

    private FanEntity createFanEntity(int id) {
        return FanEntity.builder().id(id).build();
    }
}