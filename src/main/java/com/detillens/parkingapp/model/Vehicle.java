package com.detillens.parkingapp.model;

import com.detillens.parkingapp.parking.VehicleType;
import lombok.Getter;

@Getter
public class Vehicle {
    final String registrationNumber;
    final VehicleType type;

    public Vehicle(final String registrationNumber, final VehicleType type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }
}
