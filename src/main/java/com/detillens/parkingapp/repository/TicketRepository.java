package com.detillens.parkingapp.repository;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.repository.exception.TicketOperationsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketRepository {

    private final Map<String, Ticket> tickets;

    public Optional<Ticket> getActiveTicket(final String registrationNumber) {
        return Optional.ofNullable(tickets.get(registrationNumber.toUpperCase()))
                       .filter(Ticket::isActive);
    }

    public void create(final Ticket ticket) {
        final var activeTicket = getActiveTicket(ticket.getVehicle()
                                                       .getRegistrationNumber());
        if (activeTicket.isPresent()) {
            throw new TicketOperationsException("Creating the new ticket when existing pending");
        }
        // chances of ticket history lost
        tickets.compute(ticket.getVehicle()
                              .getRegistrationNumber()
                              .toUpperCase(), (s, oldTicket) -> ticket);
        log.debug("tickets: {}", ticket);
    }

}
