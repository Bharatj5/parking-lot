package com.detillens.parkingapp.service.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoSpaceAvailableTest {

    @Test
    void testConstructor() {
        final NoSpaceAvailable classUnderTest = new NoSpaceAvailable();
        assertThat(classUnderTest.getMessage()).isEqualTo(NoSpaceAvailable.ERROR_MESSAGE);
    }

}