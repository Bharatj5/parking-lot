package com.detillens.parkingapp.repository;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.repository.exception.TicketOperationsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TicketRepositoryTest {

    @InjectMocks
    private TicketRepository classUnderTest;

    @Test
    void getActiveTicket_multipleActiveTickets_shouldReturnTicket() {
        //Given
        final var activeTicket_1 = createTicket(true);
        final var registrationNumber_1 = activeTicket_1.getVehicle()
                                                       .getRegistrationNumber();
        final var activeTicket_2 = createTicket(true);
        final var activeRegistrationNumber_2 = activeTicket_2.getVehicle()
                                                             .getRegistrationNumber();
        final TicketRepository classUnderTest = new TicketRepository(Map.of(registrationNumber_1, activeTicket_1, activeRegistrationNumber_2, activeTicket_2));

        //When
        final var actualActiveTicket = classUnderTest.getActiveTicket(registrationNumber_1);

        //Then
        assertThat(actualActiveTicket).containsSame(activeTicket_1);
    }


    @Test
    void getActiveTicket_searchForInactiveTicket_shouldNotReturnTicket() {
        //Given
        final var activeTicket_1 = createTicket(false);
        final var registrationNumber_1 = activeTicket_1.getVehicle()
                                                       .getRegistrationNumber();
        final var activeTicket_2 = createTicket(true);
        final var activeRegistrationNumber_2 = activeTicket_2.getVehicle()
                                                             .getRegistrationNumber();
        final TicketRepository classUnderTest = new TicketRepository(Map.of(registrationNumber_1, activeTicket_1, activeRegistrationNumber_2, activeTicket_2));

        //When
        final var actualActiveTicket = classUnderTest.getActiveTicket(registrationNumber_1);

        //Then
        assertThat(actualActiveTicket).isEmpty();
    }

    @Test
    void create_validCreation_shouldSaveNewTicket() {
        //Given
        final var existingInactiveTicket = createTicket(false);
        final var registrationNumber = existingInactiveTicket.getVehicle()
                                                             .getRegistrationNumber();
        final var vehicleRevisitTicket = new Ticket(existingInactiveTicket.getVehicle());
        final Map<String, Ticket> ticketMap = new ConcurrentHashMap<>();
        ticketMap.put(registrationNumber, existingInactiveTicket);
        final TicketRepository classUnderTest = new TicketRepository(ticketMap);

        //When
        classUnderTest.create(vehicleRevisitTicket);

        //Then
        assertThat(classUnderTest.getActiveTicket(registrationNumber)).containsSame(vehicleRevisitTicket);
    }

    @Test
    void create_invalidCreation_shouldThrowAnError() {
        //Given
        final var existingActiveTicket = createTicket(true);
        final var registrationNumber = existingActiveTicket.getVehicle()
                                                             .getRegistrationNumber();
        final var vehicleRevisitTicket = new Ticket(existingActiveTicket.getVehicle());
        final Map<String, Ticket> ticketMap = new ConcurrentHashMap<>();
        ticketMap.put(registrationNumber, existingActiveTicket);
        final TicketRepository classUnderTest = new TicketRepository(ticketMap);

        //When - Then
        assertThatExceptionOfType(TicketOperationsException.class).isThrownBy(() -> classUnderTest.create(vehicleRevisitTicket));
        assertThat(classUnderTest.getActiveTicket(registrationNumber)).containsSame(existingActiveTicket);
    }

    private static Ticket createTicket(final boolean active) {
        final Vehicle vehicle = Vehicle.builder()
                                       .registrationNumber(UUID.randomUUID()
                                                               .toString().toUpperCase())
                                       .type(VehicleType.CAR)
                                       .build();
        final Ticket ticket = new Ticket(vehicle);
        if (!active) {
            ticket.setCharges(10d);
            ticket.setCheckOutTime(LocalDateTime.now()
                                                .plusMinutes(40));
        }
        return ticket;
    }
}