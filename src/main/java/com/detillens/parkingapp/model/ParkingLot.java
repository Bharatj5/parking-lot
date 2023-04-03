package com.detillens.parkingapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(builderMethodName = "configure")
@Data
public final class ParkingLot {

    private final List<Slot> carSlots;
    private final Double carPricePerHour;
    private final List<Slot> motorcycleSlots;
    private final Double motorcyclePricePerHour;

}
