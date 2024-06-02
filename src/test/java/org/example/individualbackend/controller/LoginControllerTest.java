package org.example.individualbackend.controller;

import org.example.individualbackend.business.login_service.interfaces.LoginUseCase;
import org.example.individualbackend.domain.login.LoginRequest;
import org.example.individualbackend.domain.login.TokenResponse;
import org.example.individualbackend.domain.login.RegisterRequest;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @Test
     void login_UserLogsInSuccessfully() throws Exception {
        TokenResponse tokenResponse = TokenResponse.builder().accessToken("token").build();

        when(loginUseCase.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/tokens")

                .content("{\"email\":\"testemail@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"accessToken\":\"token\"}"));
    }

    @Test
    void register_UserRegistersSuccessfully() throws Exception {
        TokenResponse registerResponse = TokenResponse.builder().accessToken("token").build();
        when(loginUseCase.register(any(RegisterRequest.class))).thenReturn(registerResponse);

        mockMvc.perform(post("/tokens/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\", \"fname\":\"Johnson\", \"lname\":\"Doherty\",\"picture\":\"johnpic.png\",\"password\":\"Password_1223\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"accessToken\":\"token\"}"));
    }


    @Test
    void login_LoginException_ReturnsBadRequest() throws Exception {
        when(loginUseCase.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Login failed"));

        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testemail@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void register_RegistrationException_ReturnsBadRequest() throws Exception {
        when(loginUseCase.register(any(RegisterRequest.class))).thenThrow(new RuntimeException("Registration failed"));

        mockMvc.perform(post("/tokens/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\", \"fname\":\"Johnson\", \"lname\":\"Doherty\",\"picture\":\"johnpic.png\",\"password\":\"password1223\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_InvalidRequest_failsToRegister() throws Exception {
        //Arrange
        when(loginUseCase.register(any(RegisterRequest.class))).thenReturn(null);

        //Act
        //Assert
        mockMvc.perform(post("/tokens/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalidEmail.com\",\"fname\":\"Johnson\", \"lname\":\"Doherty\",\"picture\":\"johnpic.png\",\"password\":\"password1223\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void register_MissingEmail_ReturnsBadRequest() throws Exception{
        //Act
        //Assert
        mockMvc.perform(post("/tokens/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fname\":\"Johnson\", \"lname\":\"Doherty\",\"picture\":\"johnpic.png\",\"password\":\"password1223\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_MissingPassword_ReturnsBadRequest() throws Exception {
        //Act
        //Assert
        mockMvc.perform(post("/tokens/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalidEmail.com\",\"fname\":\"Johnson\", \"lname\":\"Doherty\",\"picture\":\"johnpic.png\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
     void login_InvalidCredentials_UserDoesNotLogIn() throws Exception {

        when(loginUseCase.login(any(LoginRequest.class))).thenReturn(null);

        mockMvc.perform(post("/tokens")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"testemail@example.com\", \"password\":\"incorrectPassword\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_EmailPassword_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"incorrectPassword\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_MissingPassword_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testemail@example.com\"}"))
                .andExpect(status().isBadRequest());
    }
}