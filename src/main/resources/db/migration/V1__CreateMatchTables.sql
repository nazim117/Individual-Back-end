CREATE TABLE football_match(
    id              int not null AUTO_INCREMENT PRIMARY KEY,
    date            DATE NOT NULL,
    venueName       varchar(255),
    statusShort     varchar(10),
    homeTeamName    varchar(50),
    homeTeamLogo    varchar(255),
    homeTeamWinner  bit,
    awayTeamName    varchar(50),
    awayTeamLogo    varchar(255),
    awayTeamWinner  bit,
    goalsHome       int,
    goalsAway       int
);

/*
INSERT INTO football_match(date,venueName,statusShort,homeTeamName,homeTeamLogo, homeTeamWinner,awayTeamName,awayTeamLogo,awayTeamWinner,goalsHome,goalsAway) VALUES
('2024-04-20', 'Stadium A', 'NS', 'Team A', 'logo1.jpg', 0, 'Team B', 'logo2.jpg', 1, 5, 7),
('2024-04-21', 'Stadium B', 'FT', 'Team C', 'logo3.jpg', 0, 'Team D', 'logo4.jpg', 1, 3, 4),
('2024-04-22', 'Stadium C', 'HT', 'Team E', 'logo5.jpg', 1, 'Team F', 'logo6.jpg', 0, 5, 3)*/
