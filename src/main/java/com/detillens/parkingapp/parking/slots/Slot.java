package com.detillens.parkingapp.parking.slots;

import com.detillens.parkingapp.parking.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Slot {

    private final int slotNumber;
    private boolean isAvailable;
    private final VehicleType vehicleType;

}
