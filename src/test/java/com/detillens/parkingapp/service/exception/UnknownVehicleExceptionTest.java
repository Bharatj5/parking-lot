package com.detillens.parkingapp.service.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownVehicleExceptionTest {

    @Test
    void testConstructor() {
        final var regNumber = "MY CAR 123";
        final var expectedMessage = UnknownVehicleException.ERROR_MESSAGE + " " + regNumber;

        final UnknownVehicleException classUnderTest = new UnknownVehicleException(regNumber);

        assertThat(classUnderTest.getMessage()).isEqualTo(expectedMessage);
    }

}