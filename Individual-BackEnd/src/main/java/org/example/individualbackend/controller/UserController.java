package org.example.individualbackend.controller;

import org.example.individualbackend.controller.interfaces.UserControllerRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserControllerRepo {
}
