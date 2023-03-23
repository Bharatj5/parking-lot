package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.parking.ParkingSpace;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class CheckOutService {

    private final TicketService ticketService;
    private final PaymentService paymentService;

    public Ticket checkOut(final ParkingSpace parkingSpace, final Vehicle vehicle) {
        final Ticket ticket = ticketService.getTicket(parkingSpace, vehicle);
        final LocalDateTime checkoutTime = LocalDateTime.now();
        ticket.setCheckOutTime(checkoutTime);
        final var stayDuration = ChronoUnit.HOURS.between(ticket.getCheckInTime(), checkoutTime);
        ticket.setCharges(paymentService.calculateCharges(vehicle.getType(), stayDuration));
        parkingSpace.freeUpSlot(ticket.getSlotNumber());
        return ticket;
    }
}
