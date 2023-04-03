package com.detillens.parkingapp.model;

import com.detillens.parkingapp.model.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class Vehicle {

    private final String registrationNumber;
    private final VehicleType type;

    public static Vehicle copyOf(final Vehicle oldVehicle) {
        Objects.requireNonNull(oldVehicle);
        return new Vehicle(oldVehicle.getRegistrationNumber(), oldVehicle.getType());
    }

}
