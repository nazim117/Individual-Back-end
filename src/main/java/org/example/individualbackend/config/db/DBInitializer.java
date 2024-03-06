package org.example.individualbackend.config.db;

import lombok.AllArgsConstructor;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.persistance.MatchRepo;
import org.example.individualbackend.persistance.UserRepo;
import org.example.individualbackend.persistance.entity.MatchEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DBInitializer {
    private MatchRepo matchRepo;
    private UserRepo userRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void populateWithMatchDummyData(){
        if (matchRepo.count() == 0) {
            matchRepo.save(MatchEntity.builder()
                    .homeTeamName("PSV")
                    .awayTeamName("Ajax")
                    .build());
            matchRepo.save(MatchEntity.builder()
                    .homeTeamName("Bayern Munich")
                    .awayTeamName("Borusia Dortmund")
                    .build());
            matchRepo.save(MatchEntity.builder()
                    .homeTeamName("Real Madrid")
                    .awayTeamName("Barcelona")
                    .build());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateWithUserDummyData(){
        if (userRepo.count() == 0){
            userRepo.save(UserEntity
                    .builder()
                    .fName("Ivan")
                    .lName("Dimitrov")
                    .password("1234")
                    .picture("picture1")
                    .tickets(new ArrayList<Ticket>())
                    .build());
            userRepo.save(UserEntity
                    .builder()
                    .fName("John")
                    .lName("Doe")
                    .password("5678")
                    .picture("picture2")
                    .tickets(new ArrayList<Ticket>())
                    .build());
            userRepo.save(UserEntity
                    .builder()
                    .fName("Michael")
                    .lName("Smith")
                    .password("4321")
                    .picture("picture3")
                    .tickets(new ArrayList<Ticket>())
                    .build());
        }
    }
}
