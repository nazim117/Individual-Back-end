package org.example.individualbackend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NotificationsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
    void sendNotification_Success() throws Exception {
        String jsonPayload = "{\"content\": \"Test message\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void sendNotification_Failure() throws Exception {
        String jsonPayload = "{}";

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
