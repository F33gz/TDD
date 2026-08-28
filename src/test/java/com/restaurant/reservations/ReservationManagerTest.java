package com.restaurant.reservations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationManagerTest {

    @Test
    void shouldCreateReservationSuccessfully() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);

        Reservation reservation = manager.createReservation("Ana", 4, date, time);

        assertNotNull(reservation);
        assertEquals("Ana", reservation.getCustomerName());
        assertEquals(4, reservation.getPartySize());
        assertEquals(date, reservation.getDate());
        assertEquals(time, reservation.getTime());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void shouldRejectReservationWhenCustomerNameIsBlank(String customerName) {
        ReservationManager manager = new ReservationManager(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.createReservation(
                        customerName,
                        4,
                        LocalDate.of(2026, 9, 15),
                        LocalTime.of(20, 0)));
    }

    @Test
    void shouldRejectReservationWhenDateIsNull() {
        ReservationManager manager = new ReservationManager(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.createReservation(
                        "Ana",
                        4,
                        null,
                        LocalTime.of(20, 0)));
    }

    @Test
    void shouldRejectReservationWhenTimeIsNull() {
        ReservationManager manager = new ReservationManager(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.createReservation(
                        "Ana",
                        4,
                        LocalDate.of(2026, 9, 15),
                        null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldRejectReservationWhenPartySizeIsNotPositive(int partySize) {
        ReservationManager manager = new ReservationManager(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.createReservation(
                        "Ana",
                        partySize,
                        LocalDate.of(2026, 9, 15),
                        LocalTime.of(20, 0)));
    }

    @Test
    void shouldGenerateDifferentCodesForReservations() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);

        Reservation firstReservation = manager.createReservation("Ana", 4, date, time);
        Reservation secondReservation = manager.createReservation("Ben", 2, date, time);

        assertNotNull(firstReservation.getCode());
        assertNotNull(secondReservation.getCode());
        assertNotEquals(firstReservation.getCode(), secondReservation.getCode());
    }

    @Test
    void shouldRejectReservationWhenCapacityIsInsufficient() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);
        manager.createReservation("Ana", 26, date, time);

        assertThrows(
                IllegalStateException.class,
                () -> manager.createReservation("Ben", 6, date, time));
    }
}
