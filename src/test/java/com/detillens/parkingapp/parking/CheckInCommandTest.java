package com.detillens.parkingapp.parking;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.service.ParkingLotService;
import com.detillens.parkingapp.service.TicketService;
import com.detillens.parkingapp.service.exception.NoSpaceAvailable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.detillens.parkingapp.parking.CheckInCommand.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInCommandTest {

    private static final String REGISTRATION_NUMBER = "MY VEHICLE";

    @InjectMocks
    private CheckInCommand classUnderTest;
    @Mock
    private TicketService mockTicketService;
    @Mock
    private ParkingLotService mockParkingLotService;

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void validate_validEntry_validationSuccessful(final String command) {
        final Input input = new Input(command);
        final boolean vehicleNotInParkingLot = false;
        when(mockParkingLotService.isVehicleInParkingLot(REGISTRATION_NUMBER)).thenReturn(vehicleNotInParkingLot);

        assertThat(classUnderTest.validate(input));
    }

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void validate_invalidEntry_validationUnsuccessful(final String command) {
        final Input input = new Input(command);
        final boolean vehicleInParkingLot = true;
        when(mockParkingLotService.isVehicleInParkingLot(REGISTRATION_NUMBER)).thenReturn(vehicleInParkingLot);

        assertThatExceptionOfType(BadInputException.class)
                .isThrownBy(() -> classUnderTest.validate(input));
    }

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void execute_validEntry_shouldLetVehicleIn(final String command, final VehicleType vehicleType) {
        final Input input = new Input(command);
        final var vehicle = Vehicle.builder()
                                   .type(vehicleType)
                                   .registrationNumber(REGISTRATION_NUMBER)
                                   .build();
        final var ticket = new Ticket(vehicle);
        when(mockTicketService.createTicket(vehicle)).thenReturn(ticket);

        final var actualTicket = classUnderTest.execute(input);

        assertThat(actualTicket).isEqualTo(ticket);
        verify(mockParkingLotService).allocateSlot(vehicle);
        verify(mockTicketService).createTicket(vehicle);
    }

    @ParameterizedTest
    @MethodSource("validInputCommands")
    void execute_noSpaceAvailable_shouldNotLetVehicleIn(final String command, final VehicleType vehicleType) {
        final Input input = new Input(command);
        final var vehicle = Vehicle.builder()
                                   .type(vehicleType)
                                   .registrationNumber(REGISTRATION_NUMBER)
                                   .build();

        doThrow(NoSpaceAvailable.class).when(mockParkingLotService).allocateSlot(vehicle);

        assertThatExceptionOfType(NoSpaceAvailable.class)
                .isThrownBy(() -> classUnderTest.execute(input));

        verify(mockParkingLotService).allocateSlot(vehicle);
        verifyNoInteractions(mockTicketService);
    }

    private static Stream<Arguments> validInputCommands() {
        return Stream.of(
                Arguments.of(NAME + " -c=" + REGISTRATION_NUMBER, VehicleType.CAR),
                Arguments.of(NAME + " -m=" + REGISTRATION_NUMBER, VehicleType.MOTORCYCLE)
        );
    }
}
