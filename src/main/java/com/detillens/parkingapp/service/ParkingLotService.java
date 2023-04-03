package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Slot;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.repository.ParkingLotRepository;
import com.detillens.parkingapp.service.exception.NoSpaceAvailable;
import com.detillens.parkingapp.service.exception.UnknownVehicleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public synchronized void allocateSlot(final Vehicle vehicle) {
        final List<Slot> slots = parkingLotRepository.getSlots(vehicle.getType());
        final Slot slot = slots.stream()
                               .filter(Slot::isAvailable)
                               .findFirst()
                               .orElseThrow(NoSpaceAvailable::new);
        slot.block(vehicle.getRegistrationNumber());
    }

    public synchronized void freeUpSlot(final Vehicle vehicle) {
        final List<Slot> slots = parkingLotRepository.getSlots(vehicle.getType());
        final Slot allocatedSlot = slots
                .stream()
                .filter(slot -> equalsIgnoreCase(slot.getVehicleRegistrationNumber(), vehicle.getRegistrationNumber()))
                .findFirst()
                .orElseThrow(() -> new UnknownVehicleException(vehicle.getRegistrationNumber()));
        allocatedSlot.unblock();
    }

    public boolean isVehicleInParkingLot(final String registrationNumber) {
        return parkingLotRepository.find(registrationNumber)
                                   .isPresent();
    }

}
