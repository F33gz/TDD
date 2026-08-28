package com.restaurant.reservations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void shouldReportAvailabilityWhenCapacityIsAvailable() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);
        manager.createReservation("Ana", 20, date, time);

        assertTrue(manager.hasAvailability(date, time, 9));
    }

    @Test
    void shouldReportAvailabilityAtExactCapacity() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);
        manager.createReservation("Ana", 26, date, time);

        assertTrue(manager.hasAvailability(date, time, 4));
    }

    @Test
    void shouldReportNoAvailabilityWhenCapacityIsInsufficient() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);
        manager.createReservation("Ana", 26, date, time);

        assertFalse(manager.hasAvailability(date, time, 6));
    }

    @Test
    void shouldKeepCapacitySeparateForDifferentTimes() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        manager.createReservation("Ana", 30, date, LocalTime.of(20, 0));

        assertTrue(manager.hasAvailability(date, LocalTime.of(21, 0), 30));
    }

    @Test
    void shouldKeepCapacitySeparateForDifferentDates() {
        ReservationManager manager = new ReservationManager(30);
        LocalTime time = LocalTime.of(20, 0);
        manager.createReservation("Ana", 30, LocalDate.of(2026, 9, 15), time);

        assertTrue(manager.hasAvailability(LocalDate.of(2026, 9, 16), time, 30));
    }

    @Test
    void shouldCancelReservationByCode() {
        ReservationManager manager = new ReservationManager(30);
        Reservation reservation = manager.createReservation(
                "Ana",
                4,
                LocalDate.of(2026, 9, 15),
                LocalTime.of(20, 0));

        manager.cancelReservation(reservation.getCode());

        assertTrue(reservation.isCancelled());
    }

    @Test
    void shouldRejectUnknownReservationCode() {
        ReservationManager manager = new ReservationManager(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.cancelReservation("unknown-code"));
    }

    @Test
    void shouldRejectCancellingReservationTwice() {
        ReservationManager manager = new ReservationManager(30);
        Reservation reservation = manager.createReservation(
                "Ana",
                4,
                LocalDate.of(2026, 9, 15),
                LocalTime.of(20, 0));
        manager.cancelReservation(reservation.getCode());

        assertThrows(
                IllegalStateException.class,
                () -> manager.cancelReservation(reservation.getCode()));
    }

    @Test
    void shouldRestoreCapacityAfterCancellation() {
        ReservationManager manager = new ReservationManager(30);
        LocalDate date = LocalDate.of(2026, 9, 15);
        LocalTime time = LocalTime.of(20, 0);
        Reservation reservation = manager.createReservation("Ana", 30, date, time);
        assertFalse(manager.hasAvailability(date, time, 1));

        manager.cancelReservation(reservation.getCode());

        assertTrue(manager.hasAvailability(date, time, 30));
    }
}
