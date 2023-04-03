package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.repository.TicketRepository;
import com.detillens.parkingapp.service.exception.UnknownVehicleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.detillens.parkingapp.model.enums.VehicleType.CAR;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ParkingLot parkingLot;

    public Ticket createTicket(final Vehicle vehicle) {
        final Ticket ticket = new Ticket(vehicle);
        ticketRepository.create(ticket);
        return ticket;
    }

    public Ticket charge(final String vehicleRegistrationNumber) {
        final Optional<Ticket> activeTicket = ticketRepository.getActiveTicket(vehicleRegistrationNumber);
        if (activeTicket.isPresent()) {
            final var ticket = activeTicket.get();
            ticket.setCheckOutTime(LocalDateTime.now());
            ticket.setCharges(calculateCharges(ticket));
            return ticket;
        } else {
            throw new UnknownVehicleException(vehicleRegistrationNumber);
        }
    }

    // this method can be under it's own class (SRP) but keeping it simple as there is not much logic
    private Double calculateCharges(final Ticket ticket) {
        final var stayDuration = Duration.between(ticket.getCheckInTime(), ticket.getCheckOutTime());
        final int actualDurationStay = (int) Math.ceil(stayDuration.toMinutes() / 60.0);
        final var rate = getRate(ticket.getVehicle()
                                       .getType());
        return actualDurationStay > 0 ? actualDurationStay * rate : rate;
    }

    private Double getRate(final VehicleType vehicleType) {
        if (vehicleType == CAR) {
            return parkingLot.getCarPricePerHour();
        }
        return parkingLot.getMotorcyclePricePerHour();
    }

}
