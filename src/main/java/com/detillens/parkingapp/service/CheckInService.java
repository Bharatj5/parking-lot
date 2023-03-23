package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.parking.ParkingSpace;
import com.detillens.parkingapp.parking.slots.Slot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckInService {

    private final ParkingSpace parkingSpace;

    public CheckInService(final ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public Ticket checkIn(final Vehicle vehicle) {
        final Slot slot = parkingSpace.allocateSlot(vehicle.getType());
        final Ticket ticket = new Ticket(vehicle, slot.getSlotNumber());
        parkingSpace.createTicket(ticket);
        return ticket;
    }
}
