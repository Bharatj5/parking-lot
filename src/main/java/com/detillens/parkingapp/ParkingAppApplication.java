package com.detillens.parkingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ParkingAppApplication  {

    public static void main(final String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(ParkingAppApplication.class, args);
        final CommandLineReader commandLineReader = applicationContext.getBean(CommandLineReader.class);
        commandLineReader.readInputs();
    }

}
