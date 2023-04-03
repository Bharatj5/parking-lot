package com.detillens.parkingapp.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownVehicleException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Unable to find vehicle details";

    public UnknownVehicleException(final String registrationNumber) {
        super(String.format("%s %s", ERROR_MESSAGE, registrationNumber));
    }

}
