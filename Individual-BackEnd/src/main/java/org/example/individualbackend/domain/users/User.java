package org.example.individualbackend.domain.users;

import org.example.individualbackend.domain.Ticket;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private final String fName;
    private final String lName;
    private final String picture;
    private final String password;
    private List<Ticket> tickets;

    public User(int id, String fName, String lName, String picture, String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.picture = picture;
        this.password = password;
        tickets = new ArrayList<Ticket>();
    }
}
