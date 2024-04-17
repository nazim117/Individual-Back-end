CREATE TABLE ticket(
    id                  int not null AUTO_INCREMENT PRIMARY KEY,
    price               double precision NOT NULL,
    rowNum              int,
    seatNumber          int,
    user_id             int not null,
    football_match_id    int not null,
    UNIQUE (rowNum, seatNumber, user_id, football_match_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (football_match_id) REFERENCES football_match (id)
);

/*INSERT INTO ticket(price, rowNum, seatNumber, user_id, football_match_id) VALUES
(50.0, 1, 10, 1, 1),
(60.0, 2, 5, 2, 2),
(70.0, 3, 15, 3, 3)*/