package com.detillens.parkingapp.parking;

import com.detillens.parkingapp.parking.slots.Slot;
import com.detillens.parkingapp.parking.slots.SlotFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingSpaceTest {

    @Test
    void parkingSpace_initialisation_shouldCreateAParkingSpace() {
        final List<Slot> carSlots = SlotFactory.createSlots(10, VehicleType.CAR);
        final List<Slot> motorcycleSlots = SlotFactory.createSlots(10, VehicleType.MOTORCYCLE);
        final ParkingSpace cityCenterMall = new ParkingSpace("City Center Mall");
        cityCenterMall.addSlots(carSlots);
        cityCenterMall.addSlots(motorcycleSlots);

        final List<Slot> actualSlots = cityCenterMall.getSlots();

        assertThat(actualSlots).hasSize(20);
        assertThat(actualSlots).containsAll(carSlots);
        assertThat(actualSlots).containsAll(motorcycleSlots);
        assertThat(actualSlots).extracting("isAvailable")
                  .as("all spaces available when initialised")
                  .containsOnly(true);
    }

    @Test
    void parkingSpace_initialisation_done() {
        final ParkingSpace parkingSpace = new ParkingSpace("City Center Mall");
        parkingSpace.addSpaces(VehicleType.CAR, 20);
        parkingSpace.addSpaces(VehicleType.MOTORCYCLE, 10);

        parkingSpace.blockSpace(VehicleType.CAR);
        parkingSpace.blockSpace(VehicleType.CAR);
        parkingSpace.blockSpace(VehicleType.CAR);

        parkingSpace.freeUpSpace(VehicleType.CAR);

        final int space = parkingSpace.getSpace(VehicleType.CAR);
        assertThat(space).isEqualTo(18);
    }

}
