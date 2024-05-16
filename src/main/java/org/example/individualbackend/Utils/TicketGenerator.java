package org.example.individualbackend.Utils;

import org.example.individualbackend.persistance.entity.TicketEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TicketGenerator {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final double BASE_PRICE = 35.0;
    private static final double PRICE_VARIATION = 25.0;

    private TicketGenerator(){ throw new AssertionError("Ticket generator should not be instantiated");}

    public static List<TicketEntity> generateTicket(int rowCount, int seatsPerRow) {
        List<TicketEntity> tickets = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
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
        double price =  BASE_PRICE + (rowNumber * PRICE_VARIATION) + (random.nextDouble() * 10.0);
        return Math.round(price * 100.0) / 100.0;
    }
}
