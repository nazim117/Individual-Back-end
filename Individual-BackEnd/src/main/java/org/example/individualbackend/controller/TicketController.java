package org.example.individualbackend.controller;

import org.example.individualbackend.controller.interfaces.TicketControllerRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController implements TicketControllerRepo {
}
