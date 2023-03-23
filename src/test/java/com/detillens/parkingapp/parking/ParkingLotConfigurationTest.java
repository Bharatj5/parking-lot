package com.detillens.parkingapp.parking;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingLotConfigurationTest {

    @Test
    void configure_spaceForCarMotorcycle_shouldConfigureParkingLot() {
        final ParkingLotConfiguration parkingLotConfiguration = new ParkingLotConfiguration();
        final ParkingSpace parkingSpace = parkingLotConfiguration.createParkingLot("name");
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
