package com.detillens.parkingapp.model;

import lombok.Data;

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

}
