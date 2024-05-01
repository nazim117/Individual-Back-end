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

import java.time.LocalDate;
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

    @Autowired
    private FanRepo fanRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save_shouldSaveUserWithAllFields(){
        List<TicketEntity> boughtTickets = createBoughTickets();

        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1234", fan.getId(), boughtTickets);

        entityManager.persist(user);
        entityManager.flush();

        assertNotNull(user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getFName());
        assertEquals("Doe", user.getLName());
        assertEquals("profilePic.jpg", user.getPicture());
        assertEquals("password1234", user.getPassword());
        assertEquals(fan.getId(), user.getFan().getId());

    }

    private UserEntity createUserEntity(String email, String fName, String lName, String picture, String password, Integer fanId, List<TicketEntity> boughtTickets) {
        return UserEntity.builder()
                .email(email)
                .fName(fName)
                .lName(lName)
                .picture(picture)
                .password(password)
                .fan(persistFanEntity(fanId, boughtTickets))
                .build();
    }

    private FanEntity persistFanEntity(Integer fanId, List<TicketEntity> boughtTickets) {
        return FanEntity.builder().id(fanId).boughtTickets(boughtTickets).build();
    }

    @Test
    void save_shouldNotSaveUserWithInvalidData(){
        List<TicketEntity> boughtTickets = createBoughTickets();

        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("invalidemail", "John", "Doe", "profilePic.jpg", "password12345", fan.getId(), boughtTickets);

        assertThrows(ConstraintViolationException.class, () -> userRepo.save(user));
    }

    @Test
    void update_shouldUpdateUserInformation(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password123456", fan.getId(), boughtTickets);

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
        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password111",fan.getId(), boughtTickets);

        UserEntity saveduser = userRepo.save(user);
        assertNotNull(saveduser.getId());

        UserEntity retrievedUser = userRepo.findById(saveduser.getId()).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals(saveduser, retrievedUser);
    }

    @Test
    void delete_shouldDeleteUser(){
        List<TicketEntity> boughtTickets = createBoughTickets();
        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password12", fan.getId(), boughtTickets);

        UserEntity savedUser = userRepo.save(user);
        assertNotNull(savedUser.getId());

        userRepo.deleteById(savedUser.getId());

        assertFalse(userRepo.existsById(savedUser.getId()));
    }

    @Test
    void existsByEmail_shouldReturnTrueForExistingEmail(){
        List<TicketEntity> boughtTickets = createBoughTickets();

        FanEntity fan = createFanEntity(4);

        fanRepository.save(fan);

        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1", fan.getId(), boughtTickets);

        userRepo.save(user);

        assertTrue(userRepo.existsByEmail("john@example.com"));
    }

    @Test
    void findAll_shouldReturnAllUsers(){
        List<TicketEntity> boughtTickets1 = createBoughTickets();
        FanEntity fan1 = createFanEntity(4);

        fanRepository.save(fan1);

        UserEntity user1 = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password123477", fan1.getId(), boughtTickets1);

        FanEntity fan2 = createFanEntity(5);

        fanRepository.save(fan2);

        List<TicketEntity> boughtTickets2 = createBoughTickets();
        UserEntity user2 = createUserEntity("ashley@example.com", "Ashley", "Johnson", "profilePic2.jpg", "password", fan2.getId(), boughtTickets2);

        userRepo.save(user1);
        userRepo.save(user2);

        UserEntity retrievedUser1 = entityManager.find(UserEntity.class, user1.getId());
        UserEntity retrievedUser2 = entityManager.find(UserEntity.class, user2.getId());

        List<UserEntity> allUsers = new ArrayList<>();

        allUsers.add(retrievedUser1);
        allUsers.add(retrievedUser2);

        assertEquals(2, allUsers.size());
    }

    @Test
    void save_shouldNotAllowDuplicateEmail(){
        FanEntity fan1 = createFanEntity(4);

        fanRepository.save(fan1);

        List<TicketEntity> boughtTickets1 = createBoughTickets();
        UserEntity user1 = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password1234", fan1.getId(), boughtTickets1);

        userRepo.save(user1);

        FanEntity fan2 = createFanEntity(5);

        fanRepository.save(fan2);

        List<TicketEntity> boughtTickets2 = createBoughTickets();
        UserEntity user2 = createUserEntity("john@example.com", "Ashley", "Johnson", "profilePic2.jpg", "password", fan2.getId(), boughtTickets2);

        assertThrows(DataIntegrityViolationException.class, () -> userRepo.save(user2));
    }
    private List<TicketEntity> createBoughTickets() {
        List<TicketEntity> tickets = new ArrayList<>();
        FanEntity fan = createFanEntity(4);
        MatchEntity footballMatch = createMatchEntity();

        tickets.add(TicketEntity.builder()
                .id(1).price(20.0).rowNum(2).seatNumber(300).fan(fan).footballMatch(footballMatch).build());

        tickets.add(TicketEntity.builder()
                .id(2).price(10.0).rowNum(6).seatNumber(280).fan(fan).footballMatch(footballMatch).build());

        return tickets;
    }

    private MatchEntity createMatchEntity() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime matchDate = LocalDateTime.parse("2024-03-12T08:30:00-10:00", formatter);
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

    private FanEntity createFanEntity(Integer id) {
        return FanEntity.builder().id(id).build();
    }
}