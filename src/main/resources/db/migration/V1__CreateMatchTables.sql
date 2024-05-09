CREATE TABLE IF NOT EXISTS football_match(
    id              int not null AUTO_INCREMENT PRIMARY KEY,
    date            DATETIME NOT NULL,
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