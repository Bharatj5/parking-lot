package com.detillens.parkingapp;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Slot;
import com.detillens.parkingapp.model.Ticket;
import com.detillens.parkingapp.service.ParkingLotService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class ParkingAppApplicationTests {

	@Autowired
    public Map<String, Ticket> tickets;
    @MockBean
    private ParkingLot parkingLot;
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private CommandLineReader commandLineReader;

    private InputStream sysInBackup;
    private PrintStream sysOutBackup;
	public static final double CAR_FAIR_PER_HOUR = 2.5d;
	public static final double MOTORCYCLE_FAIR = 1.5d;

	@BeforeEach
    void setUp() {
        // hack to use Scanner class
        sysInBackup = System.in;
        sysOutBackup = System.out;
		configureParkingLot();
    }

    @AfterEach
    void tearDown() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }


    @Test
    void contextLoads_happyPath_shouldCheckInCheckOutVehicles() {
		final var commands = "check-in -c=MyCAR \n" +
                "check-in -c=ANOTHER_CAR \n" +
                "check-out -c= ANOTHER_CAR \n" +
                "check-in -m=My bike";
        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        commandLineReader.readInputs();

        assertVehiclesInParkingLot("MyCar", "My bike");
        assertVehiclesCannotAccessParkingLot("ANOTHER_CAR");

        assertTicketCont(3);
        assertCarCheckedOut("ANOTHER_CAR", CAR_FAIR_PER_HOUR);
        assertVehicleHasActiveTicket("MyCAR", "My bike");
    }

	@Test
    void contextLoads_checkLimits_shouldAllowOnlyLimitedVehicles() {
		final var commands = "check-in -c=MyCAR \n" + // car in (1)
                "check-in -c=ANOTHER_CAR \n" +  // car in (2)
                "check-out -c= ANOTHER_CAR \n" + //  // car out (1)
                "check-in -m=My bike \n" +
                "check-in -c=My CAR 2 \n" + // car in (2)
                "check-in -c=ETL 123 \n" + // car in (3) !
                "check-in -c=CTL 456 \n"; // car in (4) !

        final ByteArrayInputStream in = new ByteArrayInputStream(commands.getBytes());
        System.setIn(in);

        commandLineReader.readInputs();

        assertVehiclesInParkingLot("MyCAR", "My CAR 2");
        assertVehiclesCannotAccessParkingLot("ANOTHER_CAR", "ETL 123", "CTL 456");

        assertTicketCont(4);

        assertCarCheckedOut("ANOTHER_CAR", CAR_FAIR_PER_HOUR);
        assertVehicleHasActiveTicket("MyCAR", "My bike");

    }

	private void configureParkingLot() {
		when(parkingLot.getCarSlots()).thenReturn(List.of(new Slot(), new Slot()));
		when(parkingLot.getMotorcycleSlots()).thenReturn(List.of(new Slot()));
		when(parkingLot.getCarPricePerHour()).thenReturn(CAR_FAIR_PER_HOUR);
		when(parkingLot.getMotorcyclePricePerHour()).thenReturn(MOTORCYCLE_FAIR);
	}

    private void assertVehicleHasActiveTicket(final String ...registrationNumber) {
		for (final String reg : registrationNumber) {
			assertThat(tickets.get(reg.toUpperCase()).isActive())
					.isTrue();
		}
    }

    private void assertVehiclesInParkingLot(final String... registrationNumber) {
        for (final String reg : registrationNumber) {
            assertThat(parkingLotService.isVehicleInParkingLot(reg)).isTrue();
        }
    }

    private void assertVehiclesCannotAccessParkingLot(final String... registrationNumber) {
        for (final String reg : registrationNumber) {
            assertThat(parkingLotService.isVehicleInParkingLot(reg)).isFalse();
        }
    }


	private void assertCarCheckedOut(final String regNum, final Double charges) {
		final var actual = tickets.get(regNum.toUpperCase());
		assertThat(actual).as("Car is checked out")
						  .extracting("checkInTime", "checkOutTime")
						  .isNotNull();
		assertThat(actual).extracting("charges")
						  .isEqualTo(charges);
	}

	private void assertTicketCont(final int i2) {
		assertThat(tickets.values()).as("Only " + i2 + " vehicle has ticket")
									.hasSize(i2);
	}

}
