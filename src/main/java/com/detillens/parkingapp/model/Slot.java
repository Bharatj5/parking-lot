package com.detillens.parkingapp.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Slot {

    private boolean isAvailable;
    private String vehicleRegistrationNumber;

    public Slot() {
        this.isAvailable = true;
    }

    public void block(final String registrationNumber) {
        this.setAvailable(false);
        this.setVehicleRegistrationNumber(registrationNumber);
    }

    public void unblock() {
        this.setAvailable(true);
        this.setVehicleRegistrationNumber(null);
    }

    public static Slot copyOf(final Slot oldSlot) {
        Objects.requireNonNull(oldSlot);
        final var newSlot = new Slot();
        newSlot.setVehicleRegistrationNumber(oldSlot.getVehicleRegistrationNumber());
        newSlot.setAvailable(oldSlot.isAvailable());
        return oldSlot;
    }
}
