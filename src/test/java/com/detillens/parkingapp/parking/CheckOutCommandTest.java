package com.detillens.parkingapp.parking;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.service.ParkingLotService;
import com.detillens.parkingapp.service.TicketService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.detillens.parkingapp.parking.CheckOutCommand.NAME;
import static com.detillens.parkingapp.parking.CheckOutCommand.VEHICLE_NOT_FOUND_ERROR_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CheckOutCommandTest {

    private static final String REGISTRATION_NUMBER = "MY VEHICLE";

    @InjectMocks
    private CheckOutCommand classUnderTest;
    @Mock
    private TicketService mockTicketService;
    @Mock
    private ParkingLotService mockParkingLotService;

    private static Stream<Arguments> validInputCommands() {
        return Stream.of(
                Arguments.of(NAME + " -c=" + REGISTRATION_NUMBER, VehicleType.CAR),
                Arguments.of(NAME + " -m=" + REGISTRATION_NUMBER, VehicleType.MOTORCYCLE)
        );
    }

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void validate_validEntry_validationSuccessful(final String command) {
        final Input input = new Input(command);
        final boolean vehicleInParkingLot = true;
        when(mockParkingLotService.isVehicleInParkingLot(REGISTRATION_NUMBER)).thenReturn(vehicleInParkingLot);

        assertThat(classUnderTest.validate(input));
    }


    @ParameterizedTest
    @MethodSource("validInputCommands")
    void validate_invalidEntry_validationUnsuccessful(final String command) {
        final Input input = new Input(command);
        final boolean vehicleNotInParkingLot = false;
        when(mockParkingLotService.isVehicleInParkingLot(REGISTRATION_NUMBER)).thenReturn(vehicleNotInParkingLot);

        assertThatExceptionOfType(BadInputException.class)
                .isThrownBy(() -> classUnderTest.validate(input)).withMessage(VEHICLE_NOT_FOUND_ERROR_MSG);
    }

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void execute_validEntry_shouldLetVehicleIn(final String command, final VehicleType vehicleType) {
        final Input input = new Input(command);
        final var vehicle = Vehicle.builder()
                                   .type(vehicleType)
                                   .registrationNumber(REGISTRATION_NUMBER)
                                   .build();
        final Ticket ticket = createMockTicket(vehicle);
        when(mockTicketService.charge(vehicle.getRegistrationNumber())).thenReturn(ticket);

        final var actualTicket = classUnderTest.execute(input);

        assertThat(actualTicket).isEqualTo(ticket);
        verify(mockParkingLotService).freeUpSlot(vehicle);
        verify(mockTicketService).charge(vehicle.getRegistrationNumber());
    }

    private Ticket createMockTicket(final Vehicle vehicle) {
        final var ticket = new Ticket(vehicle);
        ticket.setCheckOutTime(LocalDateTime.now().plusHours(2).plusMinutes(30));
        ticket.setCharges(5.7);
        return ticket;
    }
}