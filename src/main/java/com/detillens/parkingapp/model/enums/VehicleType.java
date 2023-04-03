package com.detillens.parkingapp.model.enums;

public enum VehicleType {
    CAR("c"),
    MOTORCYCLE("m");

    private final String type;

    public String getCommandName() {
        return type;
    }

    VehicleType(final String type) {
        this.type = type;
    }

}
