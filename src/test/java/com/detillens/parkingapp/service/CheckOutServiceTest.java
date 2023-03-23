package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.parking.ParkingSpace;
import com.detillens.parkingapp.parking.VehicleType;
import com.detillens.parkingapp.service.exception.UnknownVehicleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.detillens.parkingapp.ParkingSpaceTestHelper.testParkingSpace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckOutServiceTest {

    private final ParkingSpace parkingSpace = testParkingSpace();
    private final PaymentService paymentService = new PaymentService();
    @Mock
    private TicketService mockTicketService;

    @Test
    void checkOut_carParkedForHours_shouldShowCharges() {
        final CheckOutService checkOutService = new CheckOutService(mockTicketService, paymentService);
        final Vehicle vehicle = new Vehicle("XX1 2DA", VehicleType.CAR);
        // Car parked 2 hours before
        final LocalDateTime twoHoursBefore = LocalDateTime.now()
                                                          .minusHours(2);
        try (final MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now)
                               .thenReturn(twoHoursBefore);
            final Ticket ticket = new Ticket(vehicle, parkingSpace.getSlots().get(0).getSlotNumber());
            when(mockTicketService.getTicket(parkingSpace, vehicle)).thenReturn(ticket);
        }

        final Ticket actualTicket = checkOutService.checkOut(parkingSpace, vehicle);

        assertThat(actualTicket).isNotNull();
        assertThat(actualTicket.getVehicle()).isEqualTo(vehicle);
        assertThat(actualTicket.getCheckOutTime()).isNotNull()
                                                  .isAfter(twoHoursBefore);
        assertThat(actualTicket.getCharges()).isEqualTo(4d);
    }

    @Test
    void checkOut_invalidVehicleDetails_shouldThrowException() {
        final CheckOutService checkOutService = new CheckOutService(mockTicketService, paymentService);
        final Vehicle invalidVehicleDetails = new Vehicle("XX1 2DD", VehicleType.CAR);
        when(mockTicketService.getTicket(parkingSpace, invalidVehicleDetails)).thenThrow(new UnknownVehicleException());

        assertThatExceptionOfType(UnknownVehicleException.class)
                .isThrownBy(() -> checkOutService.checkOut(parkingSpace, invalidVehicleDetails));
    }
}
