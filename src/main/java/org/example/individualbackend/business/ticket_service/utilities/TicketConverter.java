package org.example.individualbackend.business.ticket_service.utilities;

import org.example.individualbackend.domain.ticket.Ticket;
import org.example.individualbackend.persistance.entity.TicketEntity;

public class TicketConverter {

    private TicketConverter(){

    }
    public static Ticket convert(TicketEntity ticket){
        return Ticket.builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .rowNum(ticket.getRowNum())
                .seatNumber(ticket.getSeatNumber())
                .fan(ticket.getFan())
                .footballMatch(ticket.getFootballMatch())
                .build();
    }
}
