package org.example.individualbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.ticket_service.interfaces.*;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {
    private final GetTicketsUseCase getTicketsUseCase;
    private final GetTicketUseCase getTicketUseCase;
    private final CreateTicketUseCase createTicketUseCase;
    private final UpdateTicketUseCase updateTicketUseCase;
    private final DeleteTicketUseCase deleteTicketUseCase;

    @GetMapping
    public ResponseEntity<GetAllTicketsResponse> getTickets(){
        return ResponseEntity.ok(getTicketsUseCase.getAll());
    }

    @GetMapping("{id}")
    @RolesAllowed({"FOOTBALL_FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<TicketEntity> getTicket(@PathVariable(value = "id") final Integer id){
        TicketEntity ticket = getTicketUseCase.getTicket(id);
        if(ticket == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(ticket);
    }
    @GetMapping({"/matches/{matchId}"})
    @RolesAllowed({"FOOTBALL_FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<List<TicketEntity>> getTicketByMatchId(@PathVariable(value = "matchId") final Integer matchId){
        List<TicketEntity> ticket = getTicketsUseCase.getByMatchId(matchId);
        if(ticket == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(getTicketsUseCase.getByMatchId(matchId));
    }
    @GetMapping("/users/{userId}")
    @RolesAllowed({"FOOTBALL_FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<List<TicketEntity>> getTicketByUserId(@PathVariable(value = "userId") final int userId){
        try{
            List<TicketEntity> ticket = getTicketsUseCase.getByFanId(userId);

            if(ticket == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(getTicketsUseCase.getByFanId(userId));
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping
    @RolesAllowed({"FOOTBALL_FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<CreateTicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request){
        CreateTicketResponse response = null;
        try{
            response = createTicketUseCase.createTicket(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/buy-ticket/{userId}")
    @RolesAllowed({"FOOTBALL_FAN"})
    public ResponseEntity<Integer> buyTicket(@PathVariable(value = "userId") final Integer userId,
                                       @Valid @RequestBody Integer ticketId){
        Integer ticketIdResponse = 0;
        try{
            ticketIdResponse = createTicketUseCase.addFanToTicket(ticketId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketIdResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketIdResponse);
        }
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<Void> updateTicket(@PathVariable(value = "id") final Integer id,
                                           @RequestBody @Valid UpdateTicketRequest request){
        request.setId(id);
        updateTicketUseCase.updateTicket(request);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<Void> deleteTicket(@PathVariable(value = "id") final Integer id){
        deleteTicketUseCase.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
