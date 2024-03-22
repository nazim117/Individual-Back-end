package org.example.individualbackend.business.impl;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.CreateTicketUseCase;
import org.example.individualbackend.domain.create.CreateTicketRequest;
import org.example.individualbackend.domain.create.CreateTicketResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateTicketUseCaseImpl implements CreateTicketUseCase {
    @Override
    public CreateTicketResponse createTicket(CreateTicketRequest request) {
        return null;
    }
}
