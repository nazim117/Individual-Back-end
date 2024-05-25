package org.example.individualbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.user_service.interfaces.CreateUserUseCase;
import org.example.individualbackend.business.user_service.interfaces.DeleteUserUseCase;
import org.example.individualbackend.business.user_service.interfaces.GetUserUseCase;
import org.example.individualbackend.business.user_service.interfaces.GetUsersUseCase;
import org.example.individualbackend.business.user_service.interfaces.UpdateUserUseCase;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.domain.get.GetAllUsersResponse;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

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
    @RolesAllowed({"ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<GetAllUsersResponse> getUsers(){
        return ResponseEntity.ok(getUsersUseCase.getUsers());
    }

    @GetMapping("{id}")
    @RolesAllowed({"FOOTBALL_FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<UserEntity> getUser(@PathVariable(value = "id") final Integer id){
        UserEntity user = null;
        try {
            user = getUserUseCase.getUser(id);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).build();
        }
        if(user == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/search")
    @RolesAllowed({"ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<GetAllUsersResponse> searchUser(@RequestParam(value = "searchString") String search){
        return ResponseEntity.ok(getUsersUseCase.getUsersByUniversalSearch(search));
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN", "FOOTBALL_FAN", "CUSTOMER_SERVICE"})
    public ResponseEntity<Void> updateUser(@PathVariable(value = "id") final Integer id,
                                                         @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        try {
            updateUserUseCase.updateUser(request);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") final Integer id){
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
