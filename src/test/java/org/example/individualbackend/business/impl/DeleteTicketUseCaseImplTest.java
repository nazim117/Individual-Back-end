package org.example.individualbackend.business.impl;

import org.example.individualbackend.business.ticket_service.implementation.DeleteTicketUseCaseImpl;
import org.example.individualbackend.config.TestConfig;
import org.example.individualbackend.persistance.repositories.TicketRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {TestConfig.class})
class DeleteTicketUseCaseImplTest {
    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private DeleteTicketUseCaseImpl deleteTicketUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void deleteTicket_ExistingTicket_TicketGetsDeleted(){
        //Arrange
        int ticketId = 1;

        //Act
        //Assert
        assertDoesNotThrow(()-> deleteTicketUseCase.deleteTicket(ticketId));

        verify(ticketRepo).deleteById(ticketId);
    }

    @Test
     void deleteUser_InvalidId_ThrowsException(){
        //Arrange
        int invalidTicketId = 1;

        //Act
        doThrow(EmptyResultDataAccessException.class).when(ticketRepo).deleteById(invalidTicketId);

        //Assert
        assertThrows(EmptyResultDataAccessException.class, ()-> deleteTicketUseCase.deleteTicket(invalidTicketId));
    }
}