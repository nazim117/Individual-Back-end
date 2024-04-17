package org.example.individualbackend.persistance;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        UserEntity user = UserEntity.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .picture("profile1.jpg")
                .password("password123")
                .build();

        UserEntity savedUser = userRepo.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFName(), savedUser.getFName());
        assertEquals(user.getLName(), savedUser.getLName());
        assertEquals(user.getPicture(), savedUser.getPicture());
        assertEquals(user.getPassword(), savedUser.getPassword());

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

}