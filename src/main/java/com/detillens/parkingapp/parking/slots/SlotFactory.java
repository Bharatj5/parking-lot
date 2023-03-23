package com.detillens.parkingapp.parking.slots;

import com.detillens.parkingapp.parking.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class SlotFactory {

    public static List<Slot> createSlots(final int numberOfSlots, final VehicleType type) {
        final List<Slot> slots = new ArrayList<>(numberOfSlots);
        for (int i = 1; i <= numberOfSlots; i++) {
            // Generating random slotNumber (id) so that we can identify state of it during retrial
            slots.add(new Slot(generate(i), true, type));
        }
        return slots;
    }


    private static int generate(final int min) {
        // this could be other service which assign appropriate id to slot
        return min + (int)(Math.random() * ((Integer.MAX_VALUE - min) + 1));
    }
}
