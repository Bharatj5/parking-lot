package com.detillens.parkingapp.parking;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.parking.slots.Slot;
import com.detillens.parkingapp.service.exception.NoSpaceAvailable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ParkingSpace {

    @Getter
    private final String name;
    private final List<Slot> slots = new ArrayList<>();
    @Getter
    private final List<Ticket> tickets = new ArrayList<>();
    private final ConcurrentMap<VehicleType, Integer> spaces = new ConcurrentHashMap<>();

    public void addSlots(final List<Slot> newSlots) {
        slots.addAll(newSlots);
    }

    public void addSpaces(final VehicleType vehicleType, final int numberOfSpace) {
        spaces.compute(vehicleType, (type, spaces) -> spaces == null ? numberOfSpace : spaces + numberOfSpace);
    }


    public void createTicket(final Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Slot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public synchronized Slot allocateSlot(final VehicleType type) {
        final Optional<Slot> availableSlot = slots.stream()
                                                  .filter(availableSlot(type))
                                                  .findFirst();
        availableSlot.ifPresent(blockTheSlot());
        return availableSlot.orElseThrow(NoSpaceAvailable::new);
    }

    private Predicate<Slot> availableSlot(final VehicleType type) {
        return slot -> slot.getVehicleType()
                           .equals(type) && slot.isAvailable();
    }

    private Consumer<Slot> blockTheSlot() {
        return slot -> slot.setAvailable(false);
    }

    public synchronized void freeUpSlot(final int slotNumber) {
        slots.stream()
             .filter(slot -> slot.getSlotNumber() == slotNumber)
             .findFirst()
             .ifPresentOrElse(slot -> slot.setAvailable(true), () -> {
                 throw new IllegalArgumentException("Slot not found!");
             });

    }


    //////

    public void removeSpaces(final VehicleType vehicleType, final int numberOfSpace) {
        spaces.computeIfPresent(vehicleType, (type, spaces) -> {
            if (spaces > 0 && spaces >= numberOfSpace) {
                return spaces - numberOfSpace;
            }
            throw new NoSpaceAvailable();
        });
    }

    public int getSpace(final VehicleType vehicleType) {
        return spaces.getOrDefault(vehicleType, 0);
    }

    public void blockSpace(final VehicleType vehicleType) {
        removeSpaces(vehicleType, 1);
    }

    public void freeUpSpace(final VehicleType vehicleType) {
        addSpaces(vehicleType, 1);
    }
}
