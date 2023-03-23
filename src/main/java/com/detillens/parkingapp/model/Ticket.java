package com.detillens.parkingapp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {

    private final Vehicle vehicle;
    private final LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double charges;
    private final int slotNumber;

    public Ticket(final Vehicle vehicle, final int slotNumber) {
        this.vehicle = vehicle;
        this.checkInTime = LocalDateTime.now();
        this.slotNumber = slotNumber;
    }

}
