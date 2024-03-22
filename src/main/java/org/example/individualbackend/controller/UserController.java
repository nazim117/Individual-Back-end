package org.example.individualbackend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.*;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController{
    private final GetUsersUseCase getUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getUsers(){
        return ResponseEntity.ok(getUsersUseCase.getUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable(value = "id") final Integer id){
        UserEntity user = getUserUseCase.getUser(id);
        if(user == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable(value = "id") final Integer id,
                                                         @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") final Integer id){
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
