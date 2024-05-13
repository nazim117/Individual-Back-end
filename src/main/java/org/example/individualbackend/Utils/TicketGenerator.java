package org.example.individualbackend.Utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.individualbackend.persistance.TicketRepo;
import org.example.individualbackend.persistance.entity.TicketEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//TODO: MAKE SOME IMPROVEMENTS ON THE PRICE GENERATION (make the generation more precise)
@NoArgsConstructor
public enum TicketGenerator {
    INSTANCE;

    private static final Random random = new Random();
    private static final double BASE_PRICE = 30.0;
    private static final double PRICE_VARIATION = 20.0;

    public List<TicketEntity> generateTicket(int rowCount, int seatsPerRow) {
        List<TicketEntity> tickets = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                double price = generatePrice(i);
                tickets.add(buildTicket(i+1, j+1, price));
            }
        }
        return tickets;
    }

    private TicketEntity buildTicket(int rowNum, int seatNum, double price) {
        return TicketEntity.builder()
                .price(price)
                .rowNum(rowNum)
                .seatNumber(seatNum)
                .fan(null)
                .footballMatch(null)
                .build();
    }

    private double generatePrice(int rowNumber) {
        double price =  BASE_PRICE + (rowNumber * PRICE_VARIATION) + (random.nextDouble() * 10.0 - 5.0);
        return Math.round(price * 100.0) / 100.0;
    }
}
