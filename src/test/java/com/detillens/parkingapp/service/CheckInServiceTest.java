package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.parking.ParkingSpace;
import com.detillens.parkingapp.parking.VehicleType;
import com.detillens.parkingapp.service.exception.NoSpaceAvailable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.detillens.parkingapp.ParkingSpaceTestHelper.testParkingSpace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CheckInServiceTest {

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void checkIn_spaceAvailable_shouldAllocateSpace(final VehicleType checkInVehicleType) {
        final ParkingSpace cityCenterMall = testParkingSpace();
        final Vehicle vehicle = new Vehicle("XTP 123", checkInVehicleType);
        final CheckInService checkInService = new CheckInService(cityCenterMall);

        final Ticket ticket = checkInService.checkIn(vehicle);

        assertThat(ticket.getVehicle()).isEqualTo(vehicle);
        assertThat(ticket.getCheckInTime()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void checkIn_noSpaceAvailable_shouldThrowAnError(final VehicleType checkInVehicleType) {
        final ParkingSpace cityCenterMall = testParkingSpace();
        final Vehicle vehicle = new Vehicle("XTP 123", checkInVehicleType);
        final Vehicle vehicle2 = new Vehicle("TTT 1T", checkInVehicleType);
        final CheckInService checkInService = new CheckInService(cityCenterMall);
        checkInService.checkIn(vehicle);


        assertThatExceptionOfType(NoSpaceAvailable.class).as("No Space available")
                                                         .isThrownBy(() -> checkInService.checkIn(vehicle2));
    }

}
