package com.detillens.parkingapp.service;

import com.detillens.parkingapp.parking.VehicleType;

public class PaymentService {

    public Double calculateCharges(final VehicleType type, final long stayDuration) {
        return stayDuration > 0 ? stayDuration * 2 : 2d;
    }
}
