package com.detillens.parkingapp;

import com.detillens.parkingapp.parking.ParkingSpace;
import com.detillens.parkingapp.parking.VehicleType;
import com.detillens.parkingapp.parking.slots.Slot;
import com.detillens.parkingapp.parking.slots.SlotFactory;

import java.util.List;

public final class ParkingSpaceTestHelper {

    public static ParkingSpace testParkingSpace() {
        final List<Slot> carSlots = SlotFactory.createSlots(1, VehicleType.CAR);
        final List<Slot> motorcycleSlots = SlotFactory.createSlots(1, VehicleType.MOTORCYCLE);
        final ParkingSpace cityCenterMall = new ParkingSpace("City Center Mall");
        cityCenterMall.addSlots(carSlots);
        cityCenterMall.addSlots(motorcycleSlots);
        return cityCenterMall;
    }
}
