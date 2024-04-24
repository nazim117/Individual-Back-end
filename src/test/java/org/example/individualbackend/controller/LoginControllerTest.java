package org.example.individualbackend.controller;

import org.example.individualbackend.business.LoginUseCase;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO:test if the methods work

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @Test
    public void login_UserLogsInSuccessfully() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("testemail@example.com").password("password123").build();
        LoginResponse loginResponse = LoginResponse.builder().accessToken("token").build();

        when(loginUseCase.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"testemail@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"accessToken\":\"token\"}"));
    }

    @Test
    public void login_InvalidCredetials_UserDoesNotLogIn() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("testemail@example.com").password("incorrectPassword").build();

        when(loginUseCase.login(any(LoginRequest.class))).thenReturn(null);

        mockMvc.perform(post("/tokens")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"testemail@example.com\", \"password\":\"incorrectPassword\"}"))
                .andExpect(status().isBadRequest());
    }
}