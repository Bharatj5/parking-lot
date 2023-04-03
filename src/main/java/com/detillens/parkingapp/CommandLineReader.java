package com.detillens.parkingapp;

import com.detillens.parkingapp.config.Input;
import com.detillens.parkingapp.exception.BadInputException;
import com.detillens.parkingapp.parking.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandLineReader {

    private final Map<String, Command> commandHandlersMap;

    public void readInputs() {
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                final Input input = new Input(scanner.nextLine());
                final Command command = Optional.ofNullable(commandHandlersMap.get(input.getCommand()))
                                                .orElseThrow(BadInputException::new);
                if (command.validate(input)) {
                    command.execute(input);
                }
            } catch (final Exception e) {
                log.error("Error! {}", e.getMessage());
            }
        }

    }

}
