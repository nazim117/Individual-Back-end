package org.example.individualbackend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.UpdateTicketUseCase;
import org.example.individualbackend.business.UpdateUserUseCase;
import org.example.individualbackend.domain.Ticket;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.domain.update.UpdateUserRequest;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.example.individualbackend.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateTicketUseCaseImpl implements UpdateTicketUseCase {
    private final TicketRepo ticketRepo;
    @Transactional
    @Override
    public void updateTicket(UpdateTicketRequest request) {
        TicketEntity ticketEntity = ticketRepo.getTicketEntityById(request.getId());
        if(ticketEntity == null){
            throw new NullPointerException("User_ID_INVALID");
        }

        updateFields(request, ticketEntity);
    }
    
    private void updateFields(UpdateTicketRequest request, TicketEntity ticketEntity) {
        ticketEntity.setId(request.getId());
        ticketEntity.setPrice(request.getPrice());
        ticketEntity.setRowNum(request.getRowNum());
        ticketEntity.setSeatNumber(request.getSeatNumber());
        ticketRepo.save(ticketEntity);
    }
}
