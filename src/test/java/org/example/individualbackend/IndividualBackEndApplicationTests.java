package org.example.individualbackend;

import org.example.individualbackend.external_api.UnirestWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IndividualBackEndApplicationTests {
    @MockBean
    private UnirestWrapper unirestWrapper;
    @Test
    void contextLoads() {
        assertTrue(true, "Context loaded successfully");
    }

}
