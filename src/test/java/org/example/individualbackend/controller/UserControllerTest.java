package org.example.individualbackend.controller;

import org.example.individualbackend.business.*;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.domain.users.User;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {TestConfig.class})
class UserControllerTest{
    @InjectMocks
    private UserController userController;
    @Mock
    private GetUsersUseCase getUsersUseCase;
    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private UpdateUserUseCase updateUserUseCase;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetUsers(){
        Mockito.when(getUsersUseCase.getUsers()).thenReturn(createMockGetAllUsersResponse());

        ResponseEntity<GetAllUsersResponse> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetUser(){
        Mockito.when(getUserUseCase.getUser(Mockito.anyInt())).thenReturn(createMockGetUserResponse());

        ResponseEntity<UserEntity> response = userController.getUser(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testCreateUser(){
        Mockito.when(createUserUseCase.createUser(Mockito.any())).thenReturn(createMockCreateUserResponse());

        CreateUserRequest request = createSampleCreateUserRequest();

        ResponseEntity<CreateUserResponse> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testUpdateUser(){
        Mockito.doNothing().when(updateUserUseCase).updateUser(Mockito.any());

        UpdateUserRequest request = createSampleUpdateUserRequest();

        ResponseEntity<Void> response = userController.updateUser(1, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUser(){
        Mockito.doNothing().when(deleteUserUseCase).deleteUser(Mockito.anyInt());

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    private UpdateUserRequest createSampleUpdateUserRequest() {
        return UpdateUserRequest
                .builder()
                .id(1)
                .fName("Michael")
                .lName("Johnson")
                .password("1234")
                .picture("newPicture")
                .build();
    }

    private CreateUserRequest createSampleCreateUserRequest() {
        return CreateUserRequest.builder()
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