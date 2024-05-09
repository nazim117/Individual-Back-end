INSERT IGNORE INTO football_match(date,
                           venueName,
                           statusShort,
                           homeTeamName,
                           homeTeamLogo,
                           homeTeamWinner,
                           awayTeamName,
                           awayTeamLogo,
                           awayTeamWinner,
                           goalsHome,
                           goalsAway) VALUES
('2024-04-20T11:30:00-05:00', 'Stadium A', 'NS', 'Team A', 'logo1.jpg', 0, 'Team B', 'logo2.jpg', 1, 5, 7),
('2024-04-21T10:30:00-05:00', 'Stadium B', 'FT', 'Team C', 'logo3.jpg', 0, 'Team D', 'logo4.jpg', 1, 3, 4),
('2024-04-22T08:30:00-05:00', 'Stadium C', 'HT', 'Team E', 'logo5.jpg', 1, 'Team F', 'logo6.jpg', 0, 5, 3)