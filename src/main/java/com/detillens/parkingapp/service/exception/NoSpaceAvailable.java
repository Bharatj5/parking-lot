package com.detillens.parkingapp.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSpaceAvailable extends RuntimeException{

    public static final String ERROR_MESSAGE = "Sorry, parking is full.";

    public NoSpaceAvailable() {
        super(ERROR_MESSAGE);
    }
}
