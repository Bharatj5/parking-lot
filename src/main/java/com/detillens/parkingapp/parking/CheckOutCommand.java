package com.detillens.parkingapp.parking;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.service.ParkingLotService;
import com.detillens.parkingapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.detillens.parkingapp.parking.CheckOutCommand.NAME;

@Slf4j
@RequiredArgsConstructor
@Service(NAME)
public class CheckOutCommand implements Command {

    public static final String NAME = "check-out";
    public static final String COMMAND_HINT = String.format("%s %s=[car registration number] %s=[motorcycle registration number]", NAME, VehicleType.CAR.toString(), VehicleType.MOTORCYCLE.toString());
    public static final String VEHICLE_NOT_FOUND_ERROR_MSG = "Vehicle not found.";

    private final TicketService ticketService;
    private final ParkingLotService parkingLotService;

    @Override
    public boolean validate(final Input input) {
        if (!parkingLotService.isVehicleInParkingLot(input.getVehicleRegistrationNumber())) {
            throw new BadInputException(VEHICLE_NOT_FOUND_ERROR_MSG);
        }
        return true;
    }


    @Override
    public Ticket execute(final Input input) {
        final Ticket finalTicket = ticketService.charge(input.getVehicleRegistrationNumber());
        parkingLotService.freeUpSlot(finalTicket.getVehicle());
        log.info("Charges: {}", finalTicket.getCharges());
        return finalTicket;
    }
}
