package org.example.individualbackend.controller;

import org.example.individualbackend.controller.interfaces.MatchControllerRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public class MatchController implements MatchControllerRepo {
}
