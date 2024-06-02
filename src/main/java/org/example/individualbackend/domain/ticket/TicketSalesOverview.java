package org.example.individualbackend.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.individualbackend.domain.match.MatchRevenueData;
import org.example.individualbackend.domain.match.MatchTicketData;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketSalesOverview {
    private long totalTicketsSold;
    private double totalRevenue;
    private List<MatchTicketData> ticketsPerMatch;
    private List<MatchRevenueData> renvenuePerMatch;
}
