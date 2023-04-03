package com.detillens.parkingapp.config;

import com.detillens.parkingapp.command.CheckInCommand;
import com.detillens.parkingapp.command.CheckOutCommand;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.model.ParkingLot;
import com.detillens.parkingapp.model.Slot;
import com.detillens.parkingapp.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@Slf4j(topic = "configuration")
public class ParkingLotConfigurations {

    @Bean
    public ParkingLot parkingLot() {
        final Scanner scanner = new Scanner(System.in);
        final Function<Integer, List<Slot>> slotCreator = this::createSlots;
        try {
            System.out.print("Enter number of car parking slots: ");
            final int carSlots = scanner.nextInt();
            System.out.print("Enter the fair per hour for car: ");
            final double carSlotPricePerHour = scanner.nextDouble();
            System.out.print("Enter number of motorcycle parking slots: ");
            final int motorcycleSlots = scanner.nextInt();
            System.out.print("Enter the fair per hour for motorcycle: ");
            final double motorcycleSlotPricePerHour = scanner.nextDouble();
            final String s = scanner.nextLine(); //skip the next line

            final ParkingLot parkingLot = ParkingLot.configure()
                                                    .carSlots(slotCreator.apply(carSlots))
                                                    .carPricePerHour(carSlotPricePerHour)
                                                    .motorcycleSlots(slotCreator.apply(motorcycleSlots))
                                                    .motorcyclePricePerHour(motorcycleSlotPricePerHour)
                                                    .build();
            System.out.println("Parking lot has been configured successfully!");
            System.out.println("Use below commands to check-in/check-out vehicles");
            System.out.println("------------------------------------------------------------------------");
            System.out.println(CheckInCommand.COMMAND_HINT);
            System.out.println(CheckOutCommand.COMMAND_HINT);
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Thank you!\n");
            return parkingLot;
        }catch (final Exception e) {
            throw new BadInputException("Configuration unsuccessful! Please enter valid inputs.");
        }
    }

    private List<Slot> createSlots(final Integer numberOfSlots) {
        return IntStream.range(0, numberOfSlots)
                        .mapToObj(i -> new Slot())
                        .collect(Collectors.toList());
    }


    @Bean
    public Map<String, Ticket> tickets() {
        return new ConcurrentHashMap<>();
    }

}
