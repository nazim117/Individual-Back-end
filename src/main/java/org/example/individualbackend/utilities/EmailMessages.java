package org.example.individualbackend.utilities;

public class EmailMessages {
    public static final String TICKET_PURCHASE_SUBJECT = "Ticket Purchase Confirmation";
    public static final String TICKET_PURCHASE_BODY = """
            Hello ${fanName}
            
            Thank you for purchasing a ticket for ${matchHomeTeamName} vs ${matchAwayTeamName}.
            Your seat number is ${seatNumber} on row ${rowNumber}.
            
            Enjoy the game!
            
            MatchPass
            """;
    public static final String USER_REGISTRATION_SUBJECT = "User Registration Confirmation";
    public static final String USER_REGISTRATION_BODY = """
            Hello ${fanName},

            Welcome to MatchPass
            Here you will be able to support your favourite team
            Keep yourself updated with the latest match information and leagues rankings
            And attend football matches
            
            If you have any questions or remarks you can always contact customer support through our chat system
            
            This email is auto-generated, if you have any questions please contact nazim14@abv.bg
            """;
}