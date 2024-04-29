CREATE TABLE IF NOT EXISTS application_user(
    id          int not null AUTO_INCREMENT PRIMARY KEY,
    email       varchar(50) NOT NULL,
    fName       varchar(50),
    lName       varchar(50),
    picture     varchar(255),
    password    varchar(255),
    fan_id      int,
    UNIQUE (email),
    UNIQUE (password),
    FOREIGN KEY (fan_id) REFERENCES fan (id)
);