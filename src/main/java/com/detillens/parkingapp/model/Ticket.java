package com.detillens.parkingapp.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Ticket {

    private final Vehicle vehicle;
    private final LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double charges;
    private Slot slot;

    public Ticket(final Vehicle vehicle) {
        this.vehicle = vehicle;
        this.checkInTime = LocalDateTime.now();
    }

    public boolean isActive() {
       return Objects.nonNull(vehicle) && Objects.isNull(checkOutTime) && Objects.isNull(charges);
    }

}
