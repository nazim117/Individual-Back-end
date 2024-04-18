package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
        UserEntity user = createUserEntity("john@example.com", "John", "Doe", "profilePic.jpg", "password123");

        entityManager.persist(user);
        entityManager.flush();

        assertNotNull(user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getFName());
        assertEquals("Doe", user.getLName());
        assertEquals("profilePic.jpg", user.getPicture());
        assertEquals("password123", user.getPassword());

    }

    private UserEntity createUserEntity(String email, String fName, String lName, String picture, String password) {
        return UserEntity.builder()
                .email(email)
                .fName(fName)
                .lName(lName)
                .picture(picture)
                .password(password)
                .build();
    }

    @Test
    void save_shouldNotSaveUserWithInvalidData(){
        UserEntity user = UserEntity.builder()
                .email("invalidEmail")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepo.save(user));
    }

    @Test
    void update_shouldUpdateUserInformation(){
        UserEntity user = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

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
        UserEntity user = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        UserEntity saveduser = userRepo.save(user);
        assertNotNull(saveduser.getId());

        UserEntity retrievedUser = userRepo.findById(saveduser.getId()).orElse(null);

        assertNotNull(retrievedUser);
        assertEquals(saveduser, retrievedUser);
    }

    @Test
    void delete_shouldDeleteUser(){
        UserEntity user = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        UserEntity savedUser = userRepo.save(user);
        assertNotNull(savedUser.getId());

        userRepo.deleteById(savedUser.getId());

        assertFalse(userRepo.existsById(savedUser.getId()));
    }

    @Test
    void existsByEmail_shouldReturnTrueForExistingEmail(){
        UserEntity user = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        userRepo.save(user);

        assertTrue(userRepo.existsByEmail("john@example.com"));
    }

    @Test
    void findAll_shouldReturnAllUsers(){
        UserEntity user1 = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        UserEntity user2 = UserEntity.builder()
                .email("randy@example.com")
                .fName("Randy")
                .lName("Smith")
                .picture("profile2.jpg")
                .password("password456")
                .build();

        userRepo.save(user1);
        userRepo.save(user2);

        List<UserEntity> allUsers = userRepo.findAll();

        assertEquals(4, allUsers.size());
    }

    @Test
    void save_shouldNotAllowDuplicateEmail(){
        UserEntity user1 = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        userRepo.save(user1);

        UserEntity user2 = UserEntity.builder()
                .email("john@example.com")
                .fName("Randy")
                .lName("Smith")
                .picture("profile2.jpg")
                .password("password456")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userRepo.save(user2));
    }
}