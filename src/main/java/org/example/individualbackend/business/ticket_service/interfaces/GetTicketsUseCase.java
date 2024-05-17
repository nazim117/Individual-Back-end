package org.example.individualbackend.business.ticket_service.interfaces;

import org.example.individualbackend.domain.get.GetAllTicketsResponse;
import org.example.individualbackend.persistance.entity.TicketEntity;

import java.util.List;

public interface GetTicketsUseCase {
    GetAllTicketsResponse getAll();
    List<TicketEntity> getByMatchId(Integer matchId);
    List<TicketEntity> getByFanId(int fanId);
}
