package org.example.individualbackend.business.UserService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UserService.Interface.CreateUserUseCase;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepo userRepo;
    @Transactional
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userRepo.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }

        UserEntity savedUser = saveNewUser(request);

        return CreateUserResponse
                .builder()
                .id(savedUser.getId())
                .build();

    }

    private UserEntity saveNewUser(CreateUserRequest request) {
        UserEntity userEntity = UserEntity
                .builder()
                .email(request.getEmail())
                .fName(request.getFName())
                .lName(request.getLName())
                .picture(request.getPicture())
                .password(request.getPassword())
                .build();

        return userRepo.save(userEntity);
    }
}
