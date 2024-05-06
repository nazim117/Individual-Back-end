CREATE TABLE IF NOT EXISTS ticket(
    id                  int not null AUTO_INCREMENT PRIMARY KEY,
    price               double NOT NULL,
    rowNum              int,
    seatNumber          int,
    fan_id              int,
    football_match_id   int not null,
    UNIQUE (rowNum, seatNumber, fan_id, football_match_id),
    FOREIGN KEY (fan_id) REFERENCES fan (id),
    FOREIGN KEY (football_match_id) REFERENCES football_match (id)
);