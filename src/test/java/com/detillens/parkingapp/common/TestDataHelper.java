package com.detillens.parkingapp.common;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Slot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestDataHelper {

    private static List<Slot> getSlots(final int numberOfSlots) {
        return IntStream.range(0, numberOfSlots)
                        .mapToObj(i -> new Slot())
                        .collect(Collectors.toList());
    }

    public static ParkingLot configureParkingLot(final int numCarSlot, final double carSlotPrice, final int numMcSlot, final double mcSlotPrice) {
        return ParkingLot.configure()
                  .carSlots(getSlots(numCarSlot))
                  .carPricePerHour(carSlotPrice)
                  .motorcycleSlots(getSlots(numMcSlot))
                  .motorcyclePricePerHour(mcSlotPrice)
                  .build();
    }
}
