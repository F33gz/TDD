package com.restaurant.reservations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    private final int maximumCapacity;
    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationManager(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public Reservation createReservation(
            String customerName,
            int partySize,
            LocalDate date,
            LocalTime time) {
        if (customerName == null || customerName.isBlank() || date == null || time == null) {
            throw new IllegalArgumentException();
        }
        if (partySize <= 0) {
            throw new IllegalArgumentException();
        }

        if (!hasAvailability(date, time, partySize)) {
            throw new IllegalStateException();
        }

        Reservation reservation = new Reservation(customerName, partySize, date, time);
        reservations.add(reservation);
        return reservation;
    }

    public boolean hasAvailability(LocalDate date, LocalTime time, int partySize) {
        int occupiedSeats = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(date) && reservation.getTime().equals(time)) {
                occupiedSeats += reservation.getPartySize();
            }
        }
        return occupiedSeats + partySize <= maximumCapacity;
    }

    public void cancelReservation(String code) {
        for (Reservation reservation : reservations) {
            if (reservation.getCode().equals(code)) {
                reservation.cancel();
                return;
            }
        }
        throw new IllegalArgumentException();
    }
}
