package org.example.individualbackend.business.UserService.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UserService.Interface.CreateUserUseCase;
import org.example.individualbackend.domain.create.CreateUserRequest;
import org.example.individualbackend.domain.create.CreateUserResponse;
import org.example.individualbackend.persistance.FanRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.UserRoleRepo;
import org.example.individualbackend.persistance.entity.FanEntity;
import org.example.individualbackend.persistance.entity.RoleEnum;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.example.individualbackend.persistance.entity.UserRoleEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepo userRepo;
    private final FanRepo fanRepo;
    private final UserRoleRepo userRoleRepo;

    @Transactional
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userRepo.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }

        FanEntity fan = null;
        UserEntity savedUser = null;
        if(request.getRole().equals("FOOTBALL_FAN")){
            fan = saveFan();
            savedUser = saveNewUser(request, fan);
        }else{
            savedUser = saveNewUser(request);
        }

        RoleEnum role = null;
        if(request.getRole().equals("ADMIN")){
            role = RoleEnum.ADMIN;
        }else if(request.getRole().equals("CUSTOMER_SERVICE")){
            role = RoleEnum.CUSTOMER_SERVICE;
        }else {
            role = RoleEnum.FOOTBALL_FAN;
        }
        saveUserRole(role, savedUser);

        return CreateUserResponse
                .builder()
                .id(savedUser.getId())
                .build();

    }

    private FanEntity saveFan() {
        return fanRepo.save(FanEntity.builder().build());
    }

    private UserRoleEntity saveUserRole(RoleEnum role, UserEntity user) {
        return userRoleRepo.save(UserRoleEntity.builder().role(role).user(user).build());
    }


    private UserEntity saveNewUser(CreateUserRequest request, FanEntity fan) {
        UserEntity userEntity = UserEntity
                    .builder()
                    .email(request.getEmail())
                    .fName(request.getFName())
                    .lName(request.getLName())
                    .picture(request.getPicture())
                    .password(request.getPassword())
                     .fan(fan)
                    .build();

        return userRepo.save(userEntity);
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
