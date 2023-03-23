package com.detillens.parkingapp.parking;

public class ParkingLotConfiguration {

    public ParkingSpace createParkingLot(final String name) {
        return new ParkingSpace(name);
    }
}
