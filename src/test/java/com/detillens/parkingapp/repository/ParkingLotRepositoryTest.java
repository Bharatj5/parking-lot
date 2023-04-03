package com.detillens.parkingapp.repository;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Slot;
import com.detillens.parkingapp.model.enums.VehicleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingLotRepositoryTest {

    @InjectMocks
    ParkingLotRepository classUnderTest;

    @Mock
    private ParkingLot parkingLot;

    @ParameterizedTest
    @MethodSource("slotsPerVehicleType")
    void getSlots_slotsCreated_shouldReturnSlots(final VehicleType vehicleType, final List<Slot> slots) {

        lenient().when(parkingLot.getCarSlots()).thenReturn(slots);
        lenient().when(parkingLot.getMotorcycleSlots())
                 .thenReturn(slots);

        final var actualSlots = classUnderTest.getSlots(vehicleType);

        assertThat(actualSlots).hasSize(slots.size())
                         .extracting("isAvailable")
                         .containsOnly(true);
    }

    @Test
    void find_validInput_shouldReturnSlot() {
        final Slot slot = new Slot();
        slot.setVehicleRegistrationNumber("TEST VEHICLE");
        when(parkingLot.getMotorcycleSlots()).thenReturn(Collections.singletonList(slot));

        final var actualSlot = classUnderTest.find("TEST VEHICLE");

        assertThat(actualSlot).containsSame(slot);
    }

    @Test
    void find_invalidInput_shouldReturnSlot() {
        final Slot slot = new Slot();
        slot.setVehicleRegistrationNumber("TEST VEHICLE");
        when(parkingLot.getMotorcycleSlots()).thenReturn(Collections.singletonList(slot));

        final var actualSlot = classUnderTest.find("ANOTHER VEHICLE");

        assertThat(actualSlot).isEmpty();
    }

    private static Stream<Arguments> slotsPerVehicleType() {
        return Stream.of(
                Arguments.of(VehicleType.CAR, getMockSlots(6)),
                Arguments.of(VehicleType.MOTORCYCLE, getMockSlots(4))
        );
    }

    private static List<Slot> getMockSlots(final int range) {
        return IntStream.range(0, range)
                        .mapToObj(i -> new Slot())
                        .collect(Collectors.toList());
    }

}