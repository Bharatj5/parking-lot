package com.detillens.parkingapp.service;

import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Vehicle;
import com.detillens.parkingapp.model.enums.VehicleType;
import com.detillens.parkingapp.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.detillens.parkingapp.common.TestDataHelper.configureParkingLot;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class ParkingLotServiceTest {

    private ParkingLotService classUnderTest;
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    public void setUp() {
        final ParkingLot parkingLot =  configureParkingLot(1, 1.5d, 1, 5d);
        parkingLotRepository = new ParkingLotRepository(parkingLot);
        classUnderTest = new ParkingLotService(parkingLotRepository);
    }

    @RepeatedTest(40)
    public void allocateAndFreeUpCarSlots_concurrentThreads_ShouldExecuteConcurrently() throws InterruptedException {
        // Two threads to allocate and free up the same slot concurrently
        final var my_vehicle = Vehicle.builder()
                                      .registrationNumber("MY_CAR")
                                      .type(VehicleType.CAR)
                                      .build();
        final Thread allocateThread = new Thread(() -> classUnderTest.allocateSlot(my_vehicle));
        final Thread freeUpThread = new Thread(() -> classUnderTest.freeUpSlot(my_vehicle));
        allocateThread.start();
        freeUpThread.start();
        allocateThread.join();
        freeUpThread.join();


        // Assert that the slot is available after being freed up
        assertThat(parkingLotRepository.getSlots(VehicleType.CAR))
                .extracting("isAvailable")
                .contains(true);
    }

    @RepeatedTest(10)
    public void allocateAndFreeUpMotorcycleSlots_concurrentThreads_ShouldExecuteConcurrently() throws InterruptedException {
        // Two threads to allocate and free up the same slot concurrently
        final var my_vehicle = Vehicle.builder()
                                      .registrationNumber("My Motorcycle")
                                      .type(VehicleType.MOTORCYCLE)
                                      .build();

        final Thread allocateThread = new Thread(() -> classUnderTest.allocateSlot(my_vehicle));
        final Thread freeUpThread = new Thread(() -> classUnderTest.freeUpSlot(my_vehicle));
        allocateThread.start();
        freeUpThread.start();
        allocateThread.join();
        freeUpThread.join();


        // Assert that the slot is available after being freed up
        assertThat(parkingLotRepository.getSlots(VehicleType.CAR))
                .extracting("isAvailable")
                .contains(true);
    }

}