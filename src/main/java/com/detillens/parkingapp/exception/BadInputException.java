package com.detillens.parkingapp.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadInputException extends RuntimeException {

    public BadInputException(final String message) {
        super(message);
    }

    public BadInputException() {
        super("Please enter valid command");
    }
}
