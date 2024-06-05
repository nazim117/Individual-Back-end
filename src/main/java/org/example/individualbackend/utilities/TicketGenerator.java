package org.example.individualbackend.utilities;

import org.example.individualbackend.persistance.entity.TicketEntity;

import java.util.ArrayList;
import java.util.List;

public class TicketGenerator {
    private static final double BASE_PRICE = 35.0;
    private static final double PRICE_VARIATION = 25.0;

    private TicketGenerator(){ throw new AssertionError("Ticket generator should not be instantiated");}

    public static List<TicketEntity> generateTickets(int stadiumCapacity) {
        int ticketCount = Math.max(1, stadiumCapacity / 1000);
        List<TicketEntity> tickets = new ArrayList<>();

        int rowCount = (int)Math.ceil(Math.sqrt(ticketCount));
        int seatCount = (int)Math.ceil((double) ticketCount / rowCount);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < seatCount && tickets.size() < ticketCount; j++) {
                double price = generatePrice(i);
                tickets.add(buildTicket(i+1, j+1, price));
            }
        }
        return tickets;
    }

    private static TicketEntity buildTicket(int rowNum, int seatNum, double price) {
        return TicketEntity.builder()
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNum)
                .fan(null)
                .footballMatch(null)
                .build();
    }

    private static double generatePrice(int rowNumber) {
        double price =  BASE_PRICE + (rowNumber * PRICE_VARIATION * 1.5);
        return Math.round(price * 100.0) / 100.0;
    }
}
