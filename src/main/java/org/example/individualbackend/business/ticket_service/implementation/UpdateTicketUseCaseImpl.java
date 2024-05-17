package org.example.individualbackend.business.ticket_service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.individualbackend.business.ticket_service.interfaces.UpdateTicketUseCase;
import org.example.individualbackend.domain.update.UpdateTicketRequest;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
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
            throw new NullPointerException("Ticket_ID_INVALID");
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
