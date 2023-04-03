package com.detillens.parkingapp.exception;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BadInputExceptionTest {

    @Test
    void exception_hasCreated() {
        final var expectedMessage = "Invalid input";
        final BadInputException classUnderTest = new BadInputException(expectedMessage);
        assertThat(classUnderTest.getMessage()).isEqualTo(expectedMessage);
    }

}