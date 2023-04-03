package com.detillens.parkingapp.repository;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Slot;
import com.detillens.parkingapp.model.enums.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class ParkingLotRepository {

    private final ParkingLot parkingLot;

    public List<Slot> getSlots(final VehicleType type) {
        if (type == VehicleType.CAR) {
            return parkingLot.getCarSlots();
        }
        return parkingLot.getMotorcycleSlots();
    }

    public Optional<Slot> find(final String registrationNumber) {
        return Stream.concat(parkingLot.getCarSlots()
                                       .stream(), parkingLot.getMotorcycleSlots()
                                                            .stream())
                     .filter(slot -> registrationNumber.equalsIgnoreCase(slot.getVehicleRegistrationNumber()))
                     .findFirst();
    }

}
