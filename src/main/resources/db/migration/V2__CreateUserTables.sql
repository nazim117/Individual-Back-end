CREATE TABLE application_user(
    id          int not null AUTO_INCREMENT PRIMARY KEY,
    email       varchar(50) NOT NULL,
    fName       varchar(50),
    lName       varchar(50),
    picture     varchar(255),
    password    varchar(255),
    UNIQUE (email),
    UNIQUE (password)
);

/*INSERT INTO users(email, fName, lName, picture, password) VALUES
('user1@example.com', 'John', 'Doe', 'avatar1.jpg', 'password123'),
('user2@example.com', 'Jane', 'Smith', 'avatar2.jpg', 'password456'),
('user3@example.com', 'Michael', 'Johnson', 'avatar3.jpg', 'password789')*/