package com.detillens.parkingapp.repository.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TicketOperationsExceptionTest {

    @Test
    void testConstructor() {
        final var errorMessage = "error!!!";
        final TicketOperationsException classUnderTest = new TicketOperationsException(errorMessage);

        assertThat(classUnderTest.getMessage()).isEqualTo(errorMessage);
    }

}