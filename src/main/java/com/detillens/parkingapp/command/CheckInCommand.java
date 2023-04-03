package com.detillens.parkingapp.command;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.service.ParkingLotService;
import com.detillens.parkingapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.detillens.parkingapp.command.CheckInCommand.NAME;
import static com.detillens.parkingapp.model.enums.VehicleType.CAR;
import static com.detillens.parkingapp.model.enums.VehicleType.MOTORCYCLE;

@RequiredArgsConstructor
@Service(NAME)
@Slf4j
public class CheckInCommand implements Command {

    public static final String NAME = "check-in";
    public static final String COMMAND_HINT = String.format("%s -%s=[car registration number] -%s=[motorcycle registration number]", NAME, CAR.getCommandName(), MOTORCYCLE.getCommandName());

    private final TicketService ticketService;
    private final ParkingLotService parkingLotService;

    @Override
    public boolean validate(final Input input) {
        if (parkingLotService.isVehicleInParkingLot(input.getVehicleRegistrationNumber())) {
            throw new BadInputException("Vehicle already in.");
        }
        return true;
    }

    @Override
    public Ticket execute(final Input input) {
        final Vehicle vehicle = Vehicle.builder()
                                       .registrationNumber(input.getVehicleRegistrationNumber())
                                       .type(input.getVehicleType())
                                       .build();
        parkingLotService.allocateSlot(vehicle); // Allocate that slot back to ticket
        final Ticket ticket = ticketService.createTicket(vehicle);
        log.info("Space allocated for {}. Check in time {}.", vehicle.getRegistrationNumber(), ticket.getCheckInTime());
        return ticket;
    }
}
