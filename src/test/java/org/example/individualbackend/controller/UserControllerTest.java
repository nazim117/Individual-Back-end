package org.example.individualbackend.controller;

import org.example.individualbackend.business.UserService.Interface.CreateUserUseCase;
import org.example.individualbackend.business.UserService.Interface.DeleteUserUseCase;
import org.example.individualbackend.business.UserService.Interface.GetUserUseCase;
import org.example.individualbackend.business.UserService.Interface.GetUsersUseCase;
import org.example.individualbackend.business.UserService.Interface.UpdateUserUseCase;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUsersUseCase getUsersUseCase;
    @MockBean
    private GetUserUseCase getUserUseCase;
    @MockBean
    private CreateUserUseCase createUserUseCase;
    @MockBean
    private UpdateUserUseCase updateUserUseCase;
    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void testGetUsers() throws Exception{
        // Arrange
        GetAllUsersResponse response = GetAllUsersResponse
                .builder()
                .users(List.of(
                        User.builder()
                                .id(20)
                                .email("alex@johnson.com")
                                .fName("Alex")
                                .lName("Johnson")
                                .picture("picture5")
                                .password("strongPass")
                                .build(),
                        User.builder()
                                .id(21)
                                .email("test@example.com")
                                .fName("test")
                                .lName("example")
                                .picture("picture5")
                                .password("password")
                                .build()
                ))
                .build();
        when(getUsersUseCase.getUsers())
                .thenReturn(response);

        // Act
        // Assert
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {
                                  "users": [
                                      {
                                          "id": 20,
                                          "email": "alex@johnson.com",
                                          "picture": "picture5",
                                          "password": "strongPass",
                                          "lname": "Johnson",
                                          "fname": "Alex"
                                      },
                                      {
                                          "id": 21,
                                          "email": "test@example.com",
                                          "picture": "picture5",
                                          "password": "password",
                                          "lname": "example",
                                          "fname": "test"
                                      }
                                  ]
                              }
                        """));
    }
    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void testGetUser() throws Exception{
        Mockito.when(getUserUseCase.getUser(Mockito.anyInt())).thenReturn(createMockGetUserResponse());

        //ResponseEntity<UserEntity> response = userController.getUser(1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                        .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }
    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void testCreateUser() throws Exception {
        Mockito.when(createUserUseCase.createUser(Mockito.any())).thenReturn(createMockCreateUserResponse());

        CreateUserRequest request = createSampleCreateUserRequest();

        // ResponseEntity<CreateUserResponse> response = userController.createUser(request);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }
    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void testUpdateUser() throws Exception{
        Mockito.doNothing().when(updateUserUseCase).updateUser(Mockito.any());

        UpdateUserRequest request = createSampleUpdateUserRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username= "testemail@example.com", roles = {"ADMIN"})
     void testDeleteUser() throws Exception{
        Mockito.doNothing().when(deleteUserUseCase).deleteUser(Mockito.anyInt());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
                .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
    private UpdateUserRequest createSampleUpdateUserRequest() {
        return UpdateUserRequest
                .builder()
                .id(1)
                .email("michael@example.com")
                .fName("Michael")
                .lName("Johnson")
                .password("1234")
                .picture("newPicture")
                .build();
    }

    private CreateUserRequest createSampleCreateUserRequest() {
        return CreateUserRequest.builder()
                .email("john@example.com")
                .fName("John")
                .lName("Doe")
                .password("1111")
                .picture("nicePicture")
                .build();
    }

    private CreateUserResponse createMockCreateUserResponse() {
        return CreateUserResponse.builder()
                .id(1)
                .build();
    }

    private UserEntity createMockGetUserResponse() {
        return UserEntity.builder()
                .id(1)
                .fName("Will")
                .lName("Smith")
                .password("1122")
                .picture("beautifulPicture")
                .build();
    }

    private GetAllUsersResponse createMockGetAllUsersResponse() {
        List<User> userList = Arrays.asList(
                User.builder().id(1).fName("Stanley").lName("Sulivan").build(),
                User.builder().id(2).fName("William").lName("Born").build()
        );

        return GetAllUsersResponse.builder()
                .users(userList)
                .build();
    }
}