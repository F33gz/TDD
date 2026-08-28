package com.restaurant.reservations;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationManager {

    public ReservationManager(int maximumCapacity) {
    }

    public Reservation createReservation(
            String customerName,
            int partySize,
            LocalDate date,
            LocalTime time) {
        return new Reservation(customerName, partySize, date, time);
    }
}
