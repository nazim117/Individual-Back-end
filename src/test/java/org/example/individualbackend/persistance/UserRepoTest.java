package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save_shouldSaveUserWithAllFields(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1234", boughtTickets);

        entityManager.persist(user);
        entityManager.flush();

        assertNotNull(user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getFName());
        assertEquals("Doe", user.getLName());
        assertEquals("profilePic.jpg", user.getPicture());
        assertEquals("password1234", user.getPassword());

    }

    private UserEntity createUserEntity(String email, String fName, String lName, String picture, String password, List<TicketEntity> boughtTickets) {
        return UserEntity.builder()
                .email(email)
                .fName(fName)
                .lName(lName)
                .picture(picture)
                .password(password)
                .fan(FanEntity.builder().id(1).boughtTickets(boughtTickets).build())
                .build();
    }

    @Test
    void save_shouldNotSaveUserWithInvalidData(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("invalidemail", "John", "Doe", "profilePic.jpg", "password12345", boughtTickets);

        assertThrows(ConstraintViolationException.class, () -> userRepo.save(user));
    }

    @Test
    void update_shouldUpdateUserInformation(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password123456", boughtTickets);

        UserEntity savedUser = userRepo.save(user);
        assertNotNull(savedUser.getId());

        savedUser.setFName("Jane");
        savedUser.setLName("Smith");
        savedUser.setPicture("new_pic.jpg");

        UserEntity updatedUser = userRepo.save(savedUser);

        UserEntity retrievedUser = userRepo.findById(updatedUser.getId()).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals("Jane", retrievedUser.getFName());
        assertEquals("Smith", retrievedUser.getLName());
        assertEquals("new_pic.jpg", retrievedUser.getPicture());

    }

    @Test
    void retrieveById_shouldReturnUserWithMatchingId(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password098",boughtTickets);

        UserEntity saveduser = userRepo.save(user);
        assertNotNull(saveduser.getId());

        UserEntity retrievedUser = userRepo.findById(saveduser.getId()).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals(saveduser, retrievedUser);
    }

    @Test
    void delete_shouldDeleteUser(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password12", boughtTickets);

        UserEntity savedUser = userRepo.save(user);
        assertNotNull(savedUser.getId());

        userRepo.deleteById(savedUser.getId());

        assertFalse(userRepo.existsById(savedUser.getId()));
    }

    @Test
    void existsByEmail_shouldReturnTrueForExistingEmail(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1", boughtTickets);

        userRepo.save(user);

        assertTrue(userRepo.existsByEmail("john@example.com"));
    }

    @Test
    void findAll_shouldReturnAllUsers(){
        List<TicketEntity> boughtTickets1 = createBoughTickets();
        UserEntity user1 = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password123477", boughtTickets1);

        List<TicketEntity> boughtTickets2 = createBoughTickets();
        UserEntity user2 = createUserEntity("ashley@example.com", "Ashley", "Johnson", "profilePic2.jpg", "password", boughtTickets2);

        userRepo.save(user1);
        userRepo.save(user2);

        List<UserEntity> allUsers = userRepo.findAll();
        //TODO: find why it gets 4 users
        assertEquals(3, allUsers.size());
    }

    @Test
    void save_shouldNotAllowDuplicateEmail(){
        List<TicketEntity> boughtTickets1 = createBoughTickets();
        UserEntity user1 = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1234", boughtTickets1);

        userRepo.save(user1);

        List<TicketEntity> boughtTickets2 = createBoughTickets();
        UserEntity user2 = createUserEntity("john@example.com", "Ashley", "Johnson", "profilePic2.jpg", "password", boughtTickets2);

        assertThrows(DataIntegrityViolationException.class, () -> userRepo.save(user2));
    }
    private List<TicketEntity> createBoughTickets() {
        List<TicketEntity> tickets = new ArrayList<>();
        FanEntity fan = createFanEntity();
        MatchEntity footballMatch = createMatchEntity();

        tickets.add(TicketEntity.builder()
                .id(1).price(20.0).rowNum(2).seatNumber(300).fan(fan).footballMatch(footballMatch).build());

        tickets.add(TicketEntity.builder()
                .id(2).price(10.0).rowNum(6).seatNumber(280).fan(fan).footballMatch(footballMatch).build());

        return tickets;
    }

    private MatchEntity createMatchEntity() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T21:00:00", formatter);
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

    private FanEntity createFanEntity() {
        return FanEntity.builder().id(1).build();
    }
}