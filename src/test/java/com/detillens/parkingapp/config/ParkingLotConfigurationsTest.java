package com.detillens.parkingapp.config;

import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.ParkingLot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ParkingLotConfigurationsTest {

    private InputStream sysInBackup;
    private PrintStream sysOutBackup;

    @BeforeEach
    void setUp() {
        // hack to use Scanner class
        sysInBackup = System.in;
        sysOutBackup = System.out;
    }

    @AfterEach
    void tearDown() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }


    @Test
    void parkingLot_validInput_shouldCreateTheParkingLot() {
        final String commands = "5 \n 2.5 \n 5 \n 1.6 \n";
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        final ParkingLotConfigurations classUnderTest = new ParkingLotConfigurations();

        final ParkingLot actualParkingLotBean = classUnderTest.parkingLot();

        assertThat(actualParkingLotBean.getCarSlots()).hasSize(5);
    }


    @Test
    void parkingLot_validInput_shouldThrowAnError() {
        final String commands = "5 \n 2 \n";
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        final ParkingLotConfigurations classUnderTest = new ParkingLotConfigurations();

        assertThatExceptionOfType(BadInputException.class).isThrownBy(classUnderTest::parkingLot);
    }

    @Test
    void tickets_initialisation_collectionInitialised() {
        final ParkingLotConfigurations classUnderTest = new ParkingLotConfigurations();

        assertThat(classUnderTest.tickets()).isNotNull()
                                            .isInstanceOf(ConcurrentHashMap.class);
        assertThat(classUnderTest.tickets()).isEmpty();
    }
}