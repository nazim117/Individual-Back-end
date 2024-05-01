package org.example.individualbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.TicketService.Interface.*;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(getTicketsUseCase.getTickets());
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
    @PostMapping
    @RolesAllowed({"FAN", "ADMIN", "CUSTOMER_SERVICE"})
    public ResponseEntity<?> createTicket(@Valid @RequestBody CreateTicketRequest request){
        try{
            CreateTicketResponse response = createTicketUseCase.createTicket(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create ticket: " + e.getMessage());
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
