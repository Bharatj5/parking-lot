package com.detillens.parkingapp.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSpaceAvailable extends RuntimeException{

    public NoSpaceAvailable() {
      log.error("Sorry, parking is full.");
    }
}
