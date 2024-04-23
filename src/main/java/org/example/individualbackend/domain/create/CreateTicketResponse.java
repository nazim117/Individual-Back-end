package org.example.individualbackend.domain.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTicketResponse {
    private final Integer id;
}
