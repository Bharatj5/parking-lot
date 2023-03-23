package com.detillens.parkingapp.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownVehicleException extends RuntimeException {

    public UnknownVehicleException() {
        log.error("Unable to find vehicle details");
    }
}
