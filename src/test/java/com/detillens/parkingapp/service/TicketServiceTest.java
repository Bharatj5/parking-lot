package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.detillens.parkingapp.common.TestDataHelper.configureParkingLot;
import static org.assertj.core.api.Assertions.assertThat;

class TicketServiceTest {

    private TicketService classUnderTest;

    private final ParkingLot parkingLot = configureParkingLot(2, 1.5d, 1, 5d);

    @BeforeEach
    public void setUp() {
        final TicketRepository ticketRepository = new TicketRepository(new ConcurrentHashMap<>());
        classUnderTest = new TicketService(ticketRepository, parkingLot);
    }

    @Test
    void createTicket_happyPath_shouldCreateTicket() {
        final Vehicle vehicle = Vehicle.builder()
                                       .type(VehicleType.CAR)
                                       .registrationNumber("123 344")
                                       .build();

        final var ticket = classUnderTest.createTicket(vehicle);

        assertThat(ticket.getVehicle()).isEqualTo(vehicle);
        assertThat(ticket.getCheckInTime()).isNotNull();
        assertThat(ticket.getCheckOutTime()).isNull();
        assertThat(ticket.getCharges()).isNull();
    }

    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void charge_checkOutAfterStay_shouldCalculateTheCharges(final VehicleType vehicleType) {
        final Vehicle vehicle = Vehicle.builder()
                                       .type(vehicleType)
                                       .registrationNumber("123 344")
                                       .build();

        final var existingTicket = classUnderTest.createTicket(vehicle);
        ReflectionTestUtils.setField(existingTicket, "checkInTime", LocalDateTime.now()
                                                                                 .minusHours(2).minusMinutes(40));

        final var ticket = classUnderTest.charge("123 344");

        final var expectedPrice = getDefinedPricePerHour(vehicleType) * 3;

        assertThat(ticket.getCharges()).as("Charges for two horus").isEqualTo(expectedPrice);
    }


    @ParameterizedTest
    @EnumSource(VehicleType.class)
    void charge_checkOutBeforeHour_shouldCalculateMinimumCharges(final VehicleType vehicleType) {
        final Vehicle vehicle = Vehicle.builder()
                                       .type(vehicleType)
                                       .registrationNumber("123 344")
                                       .build();

        final var existingTicket = classUnderTest.createTicket(vehicle);
        ReflectionTestUtils.setField(existingTicket, "checkInTime", LocalDateTime.now()
                                                                                 .minusMinutes(20));

        final var ticket = classUnderTest.charge("123 344");

        final var definedPricePerHour = getDefinedPricePerHour(vehicleType);

        assertThat(ticket.getCharges()).as("Charges for two horus").isEqualTo(definedPricePerHour);
    }

    private Double getDefinedPricePerHour(final VehicleType vehicleType) {
        return vehicleType == VehicleType.CAR ? parkingLot.getCarPricePerHour() : parkingLot.getMotorcyclePricePerHour();
    }
}