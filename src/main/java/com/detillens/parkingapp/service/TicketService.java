package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.parking.ParkingSpace;
import com.detillens.parkingapp.service.exception.UnknownVehicleException;

import java.util.Comparator;

public class TicketService {

    public Ticket getTicket(final ParkingSpace parkingSpace, final Vehicle vehicle) {
        return parkingSpace.getTickets().stream()
                      .filter(ticket -> ticket.getVehicle()
                                              .getRegistrationNumber()
                                              .equalsIgnoreCase(vehicle.getRegistrationNumber()))
                      .max(Comparator.comparing(Ticket::getCheckInTime))
                      .orElseThrow(UnknownVehicleException::new);
    }
}
