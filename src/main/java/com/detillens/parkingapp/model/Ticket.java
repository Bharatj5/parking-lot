package com.detillens.parkingapp.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Ticket {

    private final Vehicle vehicle;
    private final LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double charges;

    public Ticket(final Vehicle vehicle) {
        this.vehicle = vehicle;
        this.checkInTime = LocalDateTime.now();
    }

    public boolean isActive() {
        return Objects.nonNull(vehicle) && Objects.isNull(checkOutTime) && Objects.isNull(charges);
    }

    public String getTotalStay() {
        final Duration duration = Duration.between(checkInTime, checkOutTime);
        long seconds = Math.abs(duration.getSeconds());
        final long hours = seconds / 3600;
        seconds -= (hours * 3600);
        final long minutes = seconds / 60;
        seconds -= (minutes * 60);
        return String.format("%s:%s:%s", hours, minutes, seconds);
    }

}
