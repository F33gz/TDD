package com.restaurant.reservations;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationManager {

    private final int maximumCapacity;

    public ReservationManager(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public Reservation createReservation(
            String customerName,
            int partySize,
            LocalDate date,
            LocalTime time) {
        if (customerName == null || date == null) {
            throw new IllegalArgumentException();
        }

        return new Reservation(customerName, partySize, date, time);
    }
}
